package work.fking.pangya.iff.model;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.iff.Iff;

public record IffCharacter(
        IffCommon common,
        String model,
        String texture1,
        String texture2,
        String texture3,
        int power,
        int control,
        int accuracy,
        int spin,
        int curve,
        int powerSlot,
        int controlSlot,
        int accuracySlot,
        int spinSlot,
        int curveSlot,
        int unknown,
        int rankS,
        int rankSPowerSlot,
        int rankSControlSlot,
        int rankSAccuracySlot,
        int rankSSpinSlot,
        int rankSCurveSlot,
        String additionalTexture,
        String unknown2
) implements IffObject {

    public static IffCharacter decode(ByteBuf buffer) {
        IffCommon common = IffCommon.decode(buffer);
        String model = Iff.readFixedLengthString(buffer, 40);
        String texture1 = Iff.readFixedLengthString(buffer, 40);
        String texture2 = Iff.readFixedLengthString(buffer, 40);
        String texture3 = Iff.readFixedLengthString(buffer, 40);
        int power = buffer.readShortLE() & 0xFFFF;
        int control = buffer.readShortLE();
        int accuracy = buffer.readShortLE();
        int spin = buffer.readShortLE();
        int curve = buffer.readShortLE();
        int powerSlot = buffer.readByte();
        int controlSlot = buffer.readByte();
        int accuracySlot = buffer.readByte();
        int spinSlot = buffer.readByte();
        int curveSlot = buffer.readByte();
        int unknown = buffer.readByte();
        int rankS = buffer.readIntLE();
        int rankSPowerSlot = buffer.readByte();
        int rankSControlSlot = buffer.readByte();
        int rankSAccuracySlot = buffer.readByte();
        int rankSSpinSlot = buffer.readByte();
        int rankSCurveSlot = buffer.readByte();
        String additionalTexture = Iff.readFixedLengthString(buffer, 40);
        String unknown2 = Iff.readFixedLengthString(buffer, 3);

        return new IffCharacter(
                common,
                model,
                texture1,
                texture2,
                texture3,
                power,
                control,
                accuracy,
                spin,
                curve,
                powerSlot,
                controlSlot,
                accuracySlot,
                spinSlot,
                curveSlot,
                unknown,
                rankS,
                rankSPowerSlot,
                rankSControlSlot,
                rankSAccuracySlot,
                rankSSpinSlot,
                rankSCurveSlot,
                additionalTexture,
                unknown2
        );
    }
}
