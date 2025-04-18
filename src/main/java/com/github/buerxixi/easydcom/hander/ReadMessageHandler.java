package com.github.buerxixi.easydcom.hander;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.log4j.Log4j2;
import com.github.buerxixi.easydcom.pojo.EventMessage;
import com.github.buerxixi.easydcom.service.EventBus;
import com.github.buerxixi.easydcom.util.BizConstant;
import com.github.buerxixi.easydcom.util.XMLUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * 读取消息
 *
 * @author <a href="mailto:liujiaqiang@outlook.com">刘家强</a>
 * @since 2025/04/02 16:58
 */
@Log4j2
public class ReadMessageHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) {

        new Thread(() -> {
            // 心跳类报文不处理
            String bizSvc = XMLUtil.getBizSvc(s); // 业务类型
            if (BizConstant.HRBT.equals(bizSvc)) { // 心跳报文处理
                return;
            }

            log.info("service send xml={}",s);

            // 业务类报文
            String rltd = XMLUtil.getRltd(s); // 关联id
            if (StringUtils.isBlank(rltd)) { // 通知类
                EventBus.noticePublish.onNext(s);
            } else { // 响应类
                EventBus.respPublish.onNext(s);
            }

        }, "in bound").start();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("连结成功,channel:{}", ctx.channel());
        EventBus.eventPublish.onNext(new EventMessage("connect success", null));
    }
}
