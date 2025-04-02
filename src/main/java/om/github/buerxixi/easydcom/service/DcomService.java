package om.github.buerxixi.easydcom.service;

import om.github.buerxixi.easydcom.exception.DCOMException;
import om.github.buerxixi.easydcom.service.impl.ClientServiceImpl;

import java.util.function.Consumer;

/**
 * @author <a href="mailto:liujiaqiang@outlook.com">Liujiaqiang</a>
 * @since 2025/04/02 16:58
 */
public class DcomService {

    private static final IClientService clientService = new ClientServiceImpl();
    private static Consumer<String> consumer = null;

    /**
     * 连接
     *
     * @param host    主机
     * @param port    端口
     * @param message 发送登录报文
     */
    public static synchronized String connect(String host, int port, String message) {
        if (clientService.isConnected()) {
            // 连接已建立
            throw new DCOMException("DCOM service is already connected");
        }
        clientService.connect(host, port);
        clientService.send(message);
        return null;
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
     *
     * @param message 发送登出报文
     */
    public static synchronized void close(String message) {
        if (!clientService.isConnected()) {
            return;
        }

        // 发送注销报文
        clientService.send(message);
        // 等待回应
        // 关闭连接
        clientService.close();
    }

    /**
     * 接受通知类消息
     */
    public static void receiveNotification(Consumer<String> consumer) {
        DcomService.consumer = consumer;
    }
}
