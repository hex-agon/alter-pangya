package work.fking.pangya.iff;

import io.netty.buffer.ByteBuf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.fking.pangya.iff.model.IffObject;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public record Iff<I extends IffObject>(
        List<I> entries,
        int binding,
        int version
) {

    private static final Logger LOGGER = LogManager.getLogger(Iff.class);
    private static final Charset KR_CHARSET = Charset.forName("euc-kr");

    public static <T extends IffObject> Iff<T> decode(ByteBuf iffBuffer, Function<ByteBuf, T> iffConstructor) {
        short entries = iffBuffer.readShortLE();
        short binding = iffBuffer.readShortLE();
        int version = iffBuffer.readIntLE();
        int entrySize = iffBuffer.readableBytes() / entries;

        List<T> iffObjects = new ArrayList<>(entries);

        for (int i = 0; i < entries; i++) {
            int position = 8 + entrySize * i;
            iffBuffer.readerIndex(position);
            iffObjects.add(iffConstructor.apply(iffBuffer));
            int readBytes = iffBuffer.readerIndex() - position;

            if (readBytes != entrySize) {
                LOGGER.warn("Unparsed content on iff object, read={}, entrySize={}", readBytes, entrySize);
            }
        }
        return new Iff<>(
                iffObjects,
                binding,
                version
        );
    }

    public static String readFixedLengthString(ByteBuf buffer, int length) {
        byte[] bytes = new byte[length];
        buffer.readBytes(bytes);
        int limit = 0;

        for (int i = 0; i < bytes.length; i++) {

            if (bytes[i] == 0) {
                limit = i;
                break;
            }
        }
        return new String(bytes, 0, limit, KR_CHARSET);
    }

    public static LocalDateTime readIffDateTime(ByteBuf buffer) {
        int year = buffer.readShortLE();
        int month = buffer.readShortLE();
        int dayOfWeek = buffer.readShortLE();
        int day = buffer.readShortLE();
        int hour = buffer.readShortLE();
        int minute = buffer.readShortLE();
        int second = buffer.readShortLE();
        int millisecond = buffer.readShortLE();

        if (year == 0) {
            return null;
        }
        return LocalDateTime.of(year, month, day, hour, minute, second, millisecond);
    }
}
