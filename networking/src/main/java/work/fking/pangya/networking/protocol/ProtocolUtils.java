package work.fking.pangya.networking.protocol;

import io.netty.buffer.ByteBuf;
import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@UtilityClass
public class ProtocolUtils {

    public String readPString(ByteBuf buffer) {
        int size = buffer.readUnsignedShortLE();
        byte[] bytes = new byte[size];
        buffer.readBytes(bytes);

        return new String(bytes, StandardCharsets.US_ASCII);
    }

    public char[] readPStringCharArray(ByteBuf buffer) {
        int size = buffer.readUnsignedShortLE();
        char[] characters = new char[size];

        for (int i = 0; i < size; i++) {
            characters[i] = (char) buffer.readByte();
        }
        return characters;
    }

    public void writePString(ByteBuf buffer, String string) {
        buffer.writeShortLE(string.length());
        buffer.writeBytes(string.getBytes(StandardCharsets.US_ASCII));
    }

    public void writeFixedSizeString(ByteBuf buffer, String string, int size) {
        byte[] bytes = string.getBytes(StandardCharsets.US_ASCII);
        buffer.writeBytes(Arrays.copyOf(bytes, size));
    }
}
