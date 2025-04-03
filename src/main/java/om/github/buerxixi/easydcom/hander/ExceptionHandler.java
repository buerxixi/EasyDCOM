package om.github.buerxixi.easydcom.hander;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.CorruptedFrameException;
import lombok.extern.log4j.Log4j2;
import om.github.buerxixi.easydcom.util.BizUtil;

// 错误的连接必须主动断开
@Log4j2
public class ExceptionHandler extends ChannelDuplexHandler {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (cause instanceof CorruptedFrameException) {
            // 记录日志
            log.error("Invalid packet: {}", cause.getMessage());
            // 清空接收缓冲区
            ctx.channel().writeAndFlush(BizUtil.genLORQXML("0026", cause.getMessage()));
            ctx.channel().close();
        }
    }
}
