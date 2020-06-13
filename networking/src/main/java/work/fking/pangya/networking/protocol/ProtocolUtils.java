package work.fking.pangya.networking.protocol;

import io.netty.buffer.ByteBuf;
import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;

@UtilityClass
public class ProtocolUtils {

    public String readPString(ByteBuf buffer) {
        int size = buffer.readUnsignedShortLE();
        byte[] bytes = new byte[size];
        buffer.readBytes(bytes);

        return new String(bytes, StandardCharsets.US_ASCII);
    }

    public void writePString(ByteBuf buffer, String string) {
        buffer.writeShortLE(string.length());
        buffer.writeBytes(string.getBytes(StandardCharsets.US_ASCII));
    }
}
