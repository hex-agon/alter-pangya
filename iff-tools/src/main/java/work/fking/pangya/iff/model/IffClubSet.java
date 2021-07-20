package work.fking.pangya.iff.model;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.iff.Iff;

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
        int control = buffer.readShortLE();
        int accuracy = buffer.readShortLE();
        int spin = buffer.readShortLE();
        int curve = buffer.readShortLE();
        int powerSlot = buffer.readShortLE();
        int controlSlot = buffer.readShortLE();
        int accuracySlot = buffer.readShortLE();
        int spinSlot = buffer.readShortLE();
        int curveSlot = buffer.readShortLE();

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
