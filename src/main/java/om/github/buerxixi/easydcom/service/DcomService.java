package om.github.buerxixi.easydcom.service;

import om.github.buerxixi.easydcom.process.impl.ACKMPayloadProcess;
import om.github.buerxixi.easydcom.process.impl.ControlPayloadProcess;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author <a href="mailto:liujiaqiang@outlook.com">Liujiaqiang</a>
 * @since 2025/04/02 16:58
 */
public class DcomService {


    /**
     * 连接
     * TODO:用户信息 Service端
     * @param host    主机
     * @param port    端口
     * @param message 发送登录报文
     */
    public static synchronized String connect(String host, int port, String message) {

        // 连接不会报错
        ClientFuture.connect(host, port);

        // 发送注销报文
        ClientFuture.send(message, new ControlPayloadProcess());

        // 发送报文
        return null;
    }

    /**
     * 发送业务报文
     *
     * @param message 业务报文
     */
    public static List<String> send(String message) {

        // 发送注销报文
        return ClientFuture.send(message, new ACKMPayloadProcess());
    }

    /**
     * 关闭连接
     *
     * @param message 发送登出报文
     */
    public static synchronized void close(String message) {

        // 发送注销报文
        ClientFuture.send(message, new ControlPayloadProcess());

        ClientFuture.close();
    }

    /**
     * 接受通知类消息
     */
    public static void receiveNotification(Consumer<String> consumer) {
        ClientFuture.receiveNotification(consumer);
    }
}
