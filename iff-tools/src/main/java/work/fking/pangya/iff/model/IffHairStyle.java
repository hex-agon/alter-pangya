package work.fking.pangya.iff.model;

import io.netty.buffer.ByteBuf;

public record IffHairStyle(
        IffCommon common,
        int unknown
) implements IffObject {

    public static IffHairStyle decode(ByteBuf buffer) {
        IffCommon common = IffCommon.decode(buffer);
        int unknown = buffer.readIntLE();

        return new IffHairStyle(
                common,
                unknown
        );
    }
}
