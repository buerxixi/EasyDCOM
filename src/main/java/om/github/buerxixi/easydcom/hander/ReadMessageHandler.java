package om.github.buerxixi.easydcom.hander;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.log4j.Log4j2;

/**
 * 读取消息
 *
 * @author <a href="mailto:EMAIL * @author <a href="mailto:liujiaqiang@outlook.com">Liujiaqiang</a>
 * @since 2025/04/02 16:58
 */
@Log4j2
public class ReadMessageHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("连结成功,channel:{}", ctx.channel());
    }
}
