package om.github.buerxixi.easydcom.service;

import io.reactivex.rxjava3.disposables.Disposable;
import lombok.extern.log4j.Log4j2;
import om.github.buerxixi.easydcom.exception.DCOMException;
import om.github.buerxixi.easydcom.process.AbsPayloadProcess;
import om.github.buerxixi.easydcom.service.impl.ClientServiceImpl;
import om.github.buerxixi.easydcom.util.DCOMConstant;
import om.github.buerxixi.easydcom.util.XMLUtil;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

/**
 * 转换类
 *
 * @author <a href="mailto:liujiaqiang@outlook.com">Liujiaqiang</a>
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
        Disposable disposable = EventBus.eventPublish.subscribe(eventMessage -> {
            if (eventMessage.getThrowable() != null) {
                future.completeExceptionally(eventMessage.getThrowable());
            } else {
                future.complete(null);
            }
        });

        try {
            clientService.connect(host, port);
            future.get(DCOMConstant.SOCKET_TIMEOUT, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Failed to connect to DCOM", e);
            throw new DCOMException("Connection to DCOM failed: " + e.getMessage());
        } finally {
            disposable.dispose();
        }
    }

    /**
     * 发送业务报文
     * TODO: 如果登录失败应该调用 close 此处需要业务侧来处理
     *
     * @param message 业务报文
     */
    public static List<String> send(String message, AbsPayloadProcess process) {
        if (!clientService.isConnected()) {
            throw new DCOMException("DCOM service is not connected");
        }

        CompletableFuture<List<String>> future = new CompletableFuture<>();
        process.complete(future::complete);

        Disposable disposable = EventBus.respPublish.subscribe(s -> {
            if (XMLUtil.getBizMsgIdr(message).equals(XMLUtil.getRltd(s))) {
                try {
                    process.process(s);
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            }
        });

        try {
            clientService.send(message);
            return future.get(DCOMConstant.SOCKET_TIMEOUT, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            throw new DCOMException("Failed to send message: " + e.getMessage());
        } finally {
            disposable.dispose();
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
