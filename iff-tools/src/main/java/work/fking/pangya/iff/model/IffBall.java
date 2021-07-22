package work.fking.pangya.iff.model;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.iff.Iff;

public record IffBall(
        IffCommon common,
        String texture,
        String seqName,
        int bonusPang
) implements IffObject {

    public static IffBall decode(ByteBuf buffer) {
        var common = IffCommon.decode(buffer);
        var unk1 = buffer.readIntLE();
        var texture = Iff.readFixedLengthString(buffer, 40);
        var unknown2 = buffer.readIntLE();
        var unknown3 = buffer.readIntLE();
        var seqName = Iff.readFixedLengthString(buffer, 560);
        var bonusPang = buffer.readShortLE();
        var unknown5 = buffer.readShortLE();
        var unknown6 = buffer.readShortLE();
        var unknown7 = buffer.readShortLE();
        var unknown8 = buffer.readShortLE();
        var unknown9 = buffer.readShortLE();

        return new IffBall(
                common,
                texture,
                seqName,
                bonusPang
        );
    }
}
