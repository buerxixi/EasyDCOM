package om.github.buerxixi.easydcom.service;

/**
 * @author <a href="mailto:liujiaqiang@outlook.com">刘家强</a>
 * @since 2025/04/02 16:58
 */
public interface IClientService {

    /**
     * 连接
     *
     * @param host    主机
     * @param port    端口
     */
    void connect(String host, int port);

    /**
     * 发送业务报文
     *
     * @param message 业务报文
     */
    void send(String message);

    /**
     * 关闭连接
     */
    void close();

    /**
     * 获取连接状态
     */
    boolean isConnected();
}