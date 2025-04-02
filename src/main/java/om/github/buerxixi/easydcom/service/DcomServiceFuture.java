package om.github.buerxixi.easydcom.service;

import io.reactivex.rxjava3.disposables.Disposable;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import om.github.buerxixi.easydcom.exception.DCOMException;
import om.github.buerxixi.easydcom.service.impl.ClientServiceImpl;
import om.github.buerxixi.easydcom.util.DCOMConstant;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

/**
 * @author <a href="mailto:liujiaqiang@outlook.com">Liujiaqiang</a>
 * @since 2025/04/02 16:58
 */
@Log4j2
public class DcomServiceFuture {

    private static final IClientService clientService = new ClientServiceImpl();
    private static Consumer<String> consumer = null;

    static {
        EventBus.publish.subscribe(s -> {
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
            // 连接已建立
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

        clientService.connect(host, port);
        try {
            future.get(DCOMConstant.SOCKET_TIMEOUT, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            // 取消订阅
            disposable.dispose();
        }
    }

    /**
     * 发送业务报文
     *
     * @param message 业务报文
     */
    public static String send(String message) {
        if (!clientService.isConnected()) {
            throw new DCOMException("DCOM service is not connected");
            // 连接已建立
        }
        clientService.send(message);
        return null;
    }

    /**
     * 关闭连接
     */
    public static synchronized void close() {
        if (!clientService.isConnected()) {
            return;
        }

        clientService.close();
    }

    /**
     * 接受通知类消息
     */
    public static void receiveNotification(Consumer<String> consumer) {
        DcomServiceFuture.consumer = consumer;
    }
}
