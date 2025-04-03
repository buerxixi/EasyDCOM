package om.github.buerxixi.easydcom.hander;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;


/**
 * 消息解码器
 *
 * @author <a href="mailto:liujiaqiang@outlook.com">刘家强</a>
 * @since 2025/04/02 16:58
 */
@Log4j2
public class MessageEncoder extends MessageToByteEncoder<String> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, String s, ByteBuf byteBuf) throws Exception {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        // 版本号
        byteBuf.writeBytes(StandardCharsets.UTF_8.encode("01"));
        // 报文类型
        byteBuf.writeBytes(StandardCharsets.UTF_8.encode("XML"));
        // 报文长度
        byteBuf.writeBytes(StringUtils.leftPad(String.valueOf(bytes.length), 10).getBytes(StandardCharsets.UTF_8));

        // 备用字段
        byteBuf.writeBytes(StringUtils.leftPad("", 17).getBytes(StandardCharsets.UTF_8));
        // 内容
        byteBuf.writeBytes(bytes);
    }
}
