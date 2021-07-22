package work.fking.pangya.iff.model;

import io.netty.buffer.ByteBuf;

public record IffStats(
        int power,
        int control,
        int accuracy,
        int spin,
        int curve,
        int powerSlot,
        int controlSlot,
        int accuracySlot,
        int spinSlot,
        int curveSlot
) {

    public static IffStats decode(ByteBuf buffer) {
        return decode(buffer, false);
    }

    public static IffStats decode(ByteBuf buffer, boolean slotsAsBytes) {
        int power = buffer.readShortLE();
        int control = buffer.readShortLE();
        int accuracy = buffer.readShortLE();
        int spin = buffer.readShortLE();
        int curve = buffer.readShortLE();

        int powerSlot = slotsAsBytes ? buffer.readUnsignedByte() : buffer.readShortLE();
        int controlSlot = slotsAsBytes ? buffer.readUnsignedByte() : buffer.readShortLE();
        int accuracySlot = slotsAsBytes ? buffer.readUnsignedByte() : buffer.readShortLE();
        int spinSlot = slotsAsBytes ? buffer.readUnsignedByte() : buffer.readShortLE();
        int curveSlot = slotsAsBytes ? buffer.readUnsignedByte() : buffer.readShortLE();

        return new IffStats(
                power,
                control,
                accuracy,
                spin,
                curve,
                powerSlot,
                controlSlot,
                accuracySlot,
                spinSlot,
                curveSlot
        );
    }
}
