package work.fking.pangya.iff.model;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.iff.Iff;

public record IffPart(
        IffCommon common,
        int unknown1,
        int unknown2,
        int unknown3,
        int unknown4,
        int unknown5,
        int unknown6,
        String unknown7,
        String unknown8,
        String unknown9,
        String unknown10,
        String unknown11,
        String unknown12
) implements IffObject {

    public static IffPart decode(ByteBuf buffer) {
        IffCommon common = IffCommon.decode(buffer);
        int unknown1 = buffer.readShortLE();
        int unknown2 = buffer.readShortLE();
        int unknown3 = buffer.readShortLE();
        int unknown4 = buffer.readShortLE();
        int unknown5 = buffer.readShortLE();
        int unknown6 = buffer.readShortLE();
        String unknown7 = Iff.readFixedLengthString(buffer, 40);
        String unknown8 = Iff.readFixedLengthString(buffer, 40);
        String unknown9 = Iff.readFixedLengthString(buffer, 40);
        String unknown10 = Iff.readFixedLengthString(buffer, 40);
        String unknown11 = Iff.readFixedLengthString(buffer, 40);
        String unknown12 = Iff.readFixedLengthString(buffer, 40);

        for (int i = 0; i < 10; i++) {
            buffer.readShortLE();
        }
        Iff.readFixedLengthString(buffer, 40);
        buffer.readIntLE();
        buffer.readIntLE();
        buffer.readShortLE();
        buffer.readShortLE();
        buffer.readShortLE();
        buffer.readShortLE();

        return new IffPart(
                common,
                unknown1,
                unknown2,
                unknown3,
                unknown4,
                unknown5,
                unknown6,
                unknown7,
                unknown8,
                unknown9,
                unknown10,
                unknown11,
                unknown12
        );
    }
}
