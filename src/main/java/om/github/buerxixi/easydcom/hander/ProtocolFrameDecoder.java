package om.github.buerxixi.easydcom.hander;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.apache.commons.lang3.StringUtils;

import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

/**
 * @author <a href="mailto:liujiaqiang@outlook.com">刘家强</a>
 * @since 2025/04/02 16:58
 */
public class ProtocolFrameDecoder extends LengthFieldBasedFrameDecoder {
    /**
     * 深市登记结算 XML 实时报文接口规范（Ver 1.23）
     * 暂定单个报文的最大长度为 64KB
     */
    public ProtocolFrameDecoder() {
        super(65536, 5, 10, 17, 32);
    }

    @Override
    protected long getUnadjustedFrameLength(ByteBuf buf, int offset, int length, ByteOrder order) {
        buf = buf.order(order);

        // 读取版本号（2字节）
        String version = buf.toString(0, 2, StandardCharsets.UTF_8);
        if (!"01".equals(version)) throw new CorruptedFrameException("Invalid version");

        // 读取报文类型（3字节）
        String type = buf.toString(2, 3, StandardCharsets.UTF_8);
        if (!"XML".equals(type)) throw new CorruptedFrameException("Invalid type");

        // 报文长度
        String frameLength = buf.toString(offset, length, StandardCharsets.UTF_8);
        return Integer.parseInt(StringUtils.trim(frameLength));
    }

}
