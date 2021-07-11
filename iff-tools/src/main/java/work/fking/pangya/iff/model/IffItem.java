package work.fking.pangya.iff.model;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.iff.Iff;

public record IffItem(
        IffCommon common,
        int type,
        String model
) implements IffObject {

    public static IffItem decode(ByteBuf buffer) {
        IffCommon common = IffCommon.decode(buffer);
        int type = buffer.readIntLE();
        String model = Iff.readFixedLengthString(buffer, 40);

        return new IffItem(
                common,
                type,
                model
        );
    }
}
