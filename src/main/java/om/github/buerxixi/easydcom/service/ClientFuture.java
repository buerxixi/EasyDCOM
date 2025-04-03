package om.github.buerxixi.easydcom.service;

import lombok.extern.log4j.Log4j2;
import om.github.buerxixi.easydcom.exception.DCOMException;
import om.github.buerxixi.easydcom.process.AbsPayloadProcess;
import om.github.buerxixi.easydcom.process.impl.ConnectPayloadProcess;
import om.github.buerxixi.easydcom.service.impl.ClientServiceImpl;
import om.github.buerxixi.easydcom.util.DCOMConstant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

/**
 * 转换类
 *
 * @author <a href="mailto:liujiaqiang@outlook.com">刘家强</a>
 * @since 2025/04/02 16:58
 */
@Log4j2
public class ClientFuture {

    private static final IClientService clientService = new ClientServiceImpl();

    private static Consumer<String> consumer = null;

    static {
        EventBus.noticePublish.subscribe(s -> {
            if (consumer != null) {
                consumer.accept(s);
            }
        });
    }

    /**
     * 连接
     *
     * @param host 主机
     * @param port 端口
     */
    public static synchronized void connect(String host, int port) {
        if (clientService.isConnected()) {
            throw new DCOMException("DCOM service is already connected");
        }

        CompletableFuture<Void> future = new CompletableFuture<>();
        ConnectPayloadProcess payloadProcess = new ConnectPayloadProcess();
        Runnable closed = payloadProcess.addFuture(future);
        try {
            clientService.connect(host, port);
            future.get(DCOMConstant.SOCKET_TIMEOUT, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Failed to connect to DCOM", e);
            throw new DCOMException("Connection to DCOM failed: " + e.getMessage(), e);
        } finally {
            closed.run();
        }
    }

    /**
     * 发送业务报文
     * TODO: 如果登录失败应该调用 close 此处需要业务侧来处理
     *
     * @param message 业务报文
     */
    public static List<String> send(String message, AbsPayloadProcess<List<String>> payloadProcess) {
        if (!clientService.isConnected()) {
            throw new DCOMException("DCOM service is not connected");
        }

        CompletableFuture<List<String>> future = new CompletableFuture<>();
        payloadProcess.process(message);
        Runnable closed = payloadProcess.addFuture(future);

        try {
            clientService.send(message);
            return future.get(DCOMConstant.SOCKET_TIMEOUT, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            throw new DCOMException("Failed to send message: " + e.getMessage(), e);
        } finally {
            closed.run();
        }
    }

    /**
     * 关闭连接
     */
    public static synchronized void close() {
        if (clientService.isConnected()) {
            clientService.close();
        }
    }

    /**
     * 接受通知类消息
     */
    public static void receiveNotification(Consumer<String> consumer) {
        ClientFuture.consumer = consumer;
    }
}
