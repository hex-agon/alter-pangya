package work.fking.pangya.iff.model;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.iff.Iff;

public record IffPart(
        IffCommon common,
        String mpet,
        int type,
        int wearSlots,
        int unknown1,
        String texture1,
        String texture2,
        String texture3,
        String texture4,
        String texture5,
        String texture6,
        IffStats stats,
        int rentPrice
) implements IffObject {

    public static IffPart decode(ByteBuf buffer) {
        IffCommon common = IffCommon.decode(buffer);
        String mpet = Iff.readFixedLengthString(buffer, 40);
        int type = buffer.readIntLE();
        int wearSlots = buffer.readIntLE();
        int unknown1 = buffer.readIntLE();
        String texture1 = Iff.readFixedLengthString(buffer, 40);
        String texture2 = Iff.readFixedLengthString(buffer, 40);
        String texture3 = Iff.readFixedLengthString(buffer, 40);
        String texture4 = Iff.readFixedLengthString(buffer, 40);
        String texture5 = Iff.readFixedLengthString(buffer, 40);
        String texture6 = Iff.readFixedLengthString(buffer, 40);

        var stats = IffStats.decode(buffer);

        var unknown6 = Iff.readFixedLengthString(buffer, 48);
        var unk5 = buffer.readIntLE();
        var unk4 = buffer.readIntLE();
        var rentPrice = buffer.readIntLE();
        var unk3 = buffer.readIntLE();

        return new IffPart(
                common,
                mpet,
                type,
                wearSlots,
                unknown1,
                texture1,
                texture2,
                texture3,
                texture4,
                texture5,
                texture6,
                stats,
                rentPrice
        );
    }
}
