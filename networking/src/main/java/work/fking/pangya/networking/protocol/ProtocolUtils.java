package work.fking.pangya.networking.protocol;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.Arrays;

public final class ProtocolUtils {

    private ProtocolUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String readPString(ByteBuf buffer) {
        int size = buffer.readUnsignedShortLE();
        byte[] bytes = new byte[size];
        buffer.readBytes(bytes);

        return new String(bytes, StandardCharsets.US_ASCII);
    }

    public static char[] readPStringCharArray(ByteBuf buffer) {
        int size = buffer.readUnsignedShortLE();
        char[] characters = new char[size];

        for (int i = 0; i < size; i++) {
            characters[i] = (char) buffer.readByte();
        }
        return characters;
    }

    public static String readFixedSizeString(ByteBuf buffer, int size) {
        byte[] temp = new byte[size];
        buffer.readBytes(temp);
        int nullIdx = size;

        for (int i = 0; i < temp.length; i++) {

            if (temp[i] == 0) {
                nullIdx = i;
                break;
            }
        }
        return new String(temp, 0, nullIdx, StandardCharsets.US_ASCII);
    }

    public static void writePString(ByteBuf buffer, String string) {
        var bytes = string.getBytes(StandardCharsets.US_ASCII);
        buffer.writeShortLE(bytes.length);
        buffer.writeBytes(bytes);
    }

    public static void writeFixedSizeString(ByteBuf buffer, String string, int size) {
        byte[] bytes = string.getBytes(StandardCharsets.US_ASCII);
        buffer.writeBytes(Arrays.copyOf(bytes, size));
    }

    public static void writeLocalDateTime(ByteBuf target, LocalDateTime localDateTime) {
        target.writeShortLE(localDateTime.getYear());
        target.writeShortLE(localDateTime.getMonthValue());
        target.writeShortLE(localDateTime.getDayOfWeek().getValue());
        target.writeShortLE(localDateTime.getDayOfMonth());
        target.writeShortLE(localDateTime.getHour());
        target.writeShortLE(localDateTime.getMinute());
        target.writeShortLE(localDateTime.getSecond());
        target.writeShortLE(localDateTime.get(ChronoField.MILLI_OF_SECOND));
    }
}
