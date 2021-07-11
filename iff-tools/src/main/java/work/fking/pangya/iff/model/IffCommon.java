package work.fking.pangya.iff.model;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.iff.Iff;

import java.time.LocalDateTime;

public record IffCommon(
        int active,
        int id,
        String name,
        int level,
        String icon,
        int price,
        int discountPrice,
        int usedPrice,
        int shopFlags,
        int moneyFlags,
        int timeFlags,
        int time,
        int point,
        LocalDateTime availabilityStart,
        LocalDateTime availabilityEnd
) {

    public static IffCommon decode(ByteBuf buffer) {
        int active = buffer.readIntLE();
        int id = buffer.readIntLE();
        String name = Iff.readFixedLengthString(buffer, 40);
        int level = buffer.readByte();
        String icon = Iff.readFixedLengthString(buffer, 40);
        buffer.skipBytes(3);
        int price = buffer.readIntLE();
        int discountPrice = buffer.readIntLE();
        int usedPrice = buffer.readIntLE();
        int shopFlags = buffer.readByte();
        int moneyFlags = buffer.readByte();
        int timeFlags = buffer.readByte();
        int time = buffer.readByte();
        int point = buffer.readIntLE();
        buffer.readIntLE();
        buffer.readShortLE();
        buffer.readShortLE();
        buffer.readShortLE();
        buffer.readShortLE();
        buffer.readIntLE();
        buffer.readIntLE();
        buffer.readIntLE();
        LocalDateTime startTime = Iff.readIffDateTime(buffer);
        LocalDateTime endTime = Iff.readIffDateTime(buffer);

        return new IffCommon(
                active,
                id,
                name,
                level,
                icon,
                price,
                discountPrice,
                usedPrice,
                shopFlags,
                moneyFlags,
                timeFlags,
                time,
                point,
                startTime,
                endTime
        );
    }
}
