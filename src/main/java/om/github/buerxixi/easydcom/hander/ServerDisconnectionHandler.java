package om.github.buerxixi.easydcom.hander;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import lombok.extern.log4j.Log4j2;

/**
 * 服务器断开
 *
 * @author <a href="mailto:liujiaqiang@outlook.com">Liujiaqiang</a>
 * @since 2025/04/02 16:58
 */
@Log4j2
public class ServerDisconnectionHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
    }

    // 当连接断开时触发 inactive 事件
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        // channel断开
        log.error("服务端主动断开：{}", ctx.channel());
        ctx.close().sync();
    }

    // 异常断开
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("服务端异常断开：{}，原因：{}", ctx.channel(), cause.getMessage());
        ctx.close().sync();
    }
}
