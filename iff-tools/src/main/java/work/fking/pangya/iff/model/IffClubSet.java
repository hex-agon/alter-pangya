package work.fking.pangya.iff.model;

import io.netty.buffer.ByteBuf;

public record IffClubSet(
        IffCommon common,
        int woodId,
        int ironId,
        int wedgeId,
        int putterId,
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
) implements IffObject {

    public static IffClubSet decode(ByteBuf buffer) {
        IffCommon common = IffCommon.decode(buffer);
        int woodId = buffer.readIntLE();
        int ironId = buffer.readIntLE();
        int wedgeId = buffer.readIntLE();
        int putterId = buffer.readIntLE();
        int power = buffer.readShortLE();
        int control = buffer.readIntLE();
        int accuracy = buffer.readIntLE();
        int spin = buffer.readIntLE();
        int curve = buffer.readIntLE();
        int powerSlot = buffer.readIntLE();
        int controlSlot = buffer.readIntLE();
        int accuracySlot = buffer.readIntLE();
        int spinSlot = buffer.readIntLE();
        int curveSlot = buffer.readIntLE();

        return new IffClubSet(
                common,
                woodId,
                ironId,
                wedgeId,
                putterId,
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
