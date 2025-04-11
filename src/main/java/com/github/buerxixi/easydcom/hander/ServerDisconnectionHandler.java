package com.github.buerxixi.easydcom.hander;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.log4j.Log4j2;
import com.github.buerxixi.easydcom.exception.DCOMException;
import com.github.buerxixi.easydcom.pojo.EventMessage;
import com.github.buerxixi.easydcom.service.EventBus;

/**
 * 服务器断开
 *
 * @author <a href="mailto:liujiaqiang@outlook.com">刘家强</a>
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
        EventBus.eventPublish.onNext(new EventMessage(null, new DCOMException("服务端主动断开")));
        ctx.close().sync();
    }

    // 异常断开
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("服务端异常断开：{}，原因：{}", ctx.channel(), cause.getMessage());
        EventBus.eventPublish.onNext(new EventMessage(null, cause));
        ctx.close().sync();
    }
}
