package work.fking.pangya.iff.model;

import io.netty.buffer.ByteBuf;

public record IffClubSet(
        IffCommon common,
        int woodId,
        int ironId,
        int wedgeId,
        int putterId,
        IffStats stats
) implements IffObject {

    public static IffClubSet decode(ByteBuf buffer) {
        IffCommon common = IffCommon.decode(buffer);
        int woodId = buffer.readIntLE();
        int ironId = buffer.readIntLE();
        int wedgeId = buffer.readIntLE();
        int putterId = buffer.readIntLE();
        var stats = IffStats.decode(buffer);

        return new IffClubSet(
                common,
                woodId,
                ironId,
                wedgeId,
                putterId,
                stats
        );
    }
}
