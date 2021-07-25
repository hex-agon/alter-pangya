package work.fking.pangya.iff.model;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.iff.Iff;

public record IffCaddie(
        IffCommon common,
        int salary,
        String mpet,
        int power,
        int control,
        int accuracy,
        int spin,
        int curve
) implements IffObject {

    public static IffCaddie decode(ByteBuf buffer) {
        IffCommon common = IffCommon.decode(buffer);
        var salary = buffer.readIntLE();
        var mpet = Iff.readFixedLengthString(buffer, 40);
        var power = buffer.readShortLE();
        var control = buffer.readShortLE();
        var accuracy = buffer.readShortLE();
        var spin = buffer.readShortLE();
        var curve = buffer.readShortLE();

        return new IffCaddie(
                common,
                salary,
                mpet,
                power,
                control,
                accuracy,
                spin,
                curve
        );
    }
}
