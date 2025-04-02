package om.github.buerxixi.easydcom.util;

import org.apache.commons.codec.binary.Hex;

import java.nio.ByteBuffer;

public class ByteBufferUtil {
    public static void debugAll(ByteBuffer buffer) {
        int oldlimit = buffer.limit();
        buffer.limit(buffer.capacity());
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        String hex = Hex.encodeHexString(bytes);
        System.out.println("+--------+-------------------- all ------------------------+----------------+");
        System.out.printf("position: [%d], limit: [%d]\n", buffer.position(), oldlimit);
        System.out.println(hex);
        buffer.limit(oldlimit);
    }

    public static void debugRead(ByteBuffer buffer) {
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        String hex = Hex.encodeHexString(bytes);
        System.out.println("+--------+-------------------- read -----------------------+----------------+");
        System.out.printf("position: [%d], limit: [%d]\n", buffer.position(), buffer.limit());
        System.out.println(hex);
    }
}
