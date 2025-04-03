package om.github.buerxixi.easydcom.hander;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.log4j.Log4j2;
import om.github.buerxixi.easydcom.util.BizUtil;


/**
 * 心跳检测业务逻辑
 *
 * @author <a href="mailto:liujiaqiang@outlook.com">刘家强</a>
 * @since 2025/04/02 16:58
 */
@Log4j2
public class HeartbeatHandler extends ChannelDuplexHandler {
    // 特殊事件
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent event = (IdleStateEvent) evt;
        // 读空闲事件
        if (event.state() == IdleState.READER_IDLE) {
            // 异常需断开连结
            // 在发送心跳报文之后的 30 秒钟之内，如果未收到对方任何报文，则认为对方已经出现异常或离开，本次登录被自动注销，同时断开连接。
            log.error("读空闲异常服务器未在30s获取服务器响应");
            ctx.channel().close();
            // 写空闲事件
        } else if (event.state() == IdleState.WRITER_IDLE) {
            // 发送心跳报文报文
            ctx.channel().writeAndFlush(BizUtil.genHRBTXML());

            // // 非心跳事件，继续传递
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
