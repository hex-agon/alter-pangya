package work.fking.pangya.iff.model;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.iff.Iff;

public record IffCourse(
        IffCommon common,
        String shortName,
        String localizedName,
        int courseFlags,
        String propertyFileName,
        int unknown,
        String courseSequence
) implements IffObject {

    public static IffCourse decode(ByteBuf buffer) {
        IffCommon common = IffCommon.decode(buffer);
        String shortName = Iff.readFixedLengthString(buffer, 40);
        String localizedName = Iff.readFixedLengthString(buffer, 40);
        int courseFlags = buffer.readByte();
        String propertyFileName = Iff.readFixedLengthString(buffer, 40);
        int unknown = buffer.readIntLE();
        String courseSequence = Iff.readFixedLengthString(buffer, 40);

        return new IffCourse(
                common,
                shortName,
                localizedName,
                courseFlags,
                propertyFileName,
                unknown,
                courseSequence
        );
    }
}
