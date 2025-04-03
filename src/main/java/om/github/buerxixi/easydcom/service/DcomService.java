package om.github.buerxixi.easydcom.service;

import om.github.buerxixi.easydcom.process.impl.ACKMPayloadProcess;
import om.github.buerxixi.easydcom.process.impl.ControlPayloadProcess;
import om.github.buerxixi.easydcom.util.BizUtil;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author <a href="mailto:liujiaqiang@outlook.com">刘家强</a>
 * @since 2025/04/02 16:58
 */
public class DcomService {


    /**
     * 连接
     *
     * @param host     主机
     * @param port     端口
     * @param appIdr   应用系统标识符
     * @param usrIdr   用户标识/用户名(UserName)
     * @param password 密码
     */
    public static synchronized List<String> connect(String host, int port, String appIdr, String usrIdr, String password) {
        return connect(host, port, appIdr, usrIdr, password, 0);
    }

    /**
     * 连接
     *
     * @param host     主机
     * @param port     端口
     * @param appIdr   应用系统标识符
     * @param usrIdr   用户标识/用户名(UserName)
     * @param password 密码
     * @param recvHB   回报条数
     */
    public static synchronized List<String> connect(String host, int port, String appIdr, String usrIdr, String password, Integer recvHB) {

        // 连接不会报错
        ClientFuture.connect(host, port);

        // 发送注销报文
        String LIRQXML = BizUtil.genLIRQXML(appIdr, usrIdr, password, recvHB);
        return ClientFuture.send(LIRQXML, new ControlPayloadProcess());
    }

    /**
     * 发送业务报文
     *
     * @param message 业务报文
     */
    public static List<String> send(String message) {

        // 发送业务报文
        return ClientFuture.send(message, new ACKMPayloadProcess());
    }

    /**
     * 关闭连接
     */
    public static synchronized void close() {

        // 发送注销报文
        String LORQXML = BizUtil.genLORQXML();
        ClientFuture.send(LORQXML, new ControlPayloadProcess());

        ClientFuture.close();
    }

    /**
     * 接受通知类消息
     */
    public static void receiveNotification(Consumer<String> consumer) {
        ClientFuture.receiveNotification(consumer);
    }
}
