package work.fking.pangya.iff.model;

import io.netty.buffer.ByteBuf;

public record IffClubSet(
        IffCommon common,
        int woodId,
        int ironId,
        int wedgeId,
        int putterId,
        IffStats stats,
        float workshopRate
) implements IffObject {

    public static IffClubSet decode(ByteBuf buffer) {
        IffCommon common = IffCommon.decode(buffer);
        int woodId = buffer.readIntLE();
        int ironId = buffer.readIntLE();
        int wedgeId = buffer.readIntLE();
        int putterId = buffer.readIntLE();
        var stats = IffStats.decode(buffer);
        var type = buffer.readIntLE();
        var specialStatus = buffer.readIntLE();
        var recoveryLimit = buffer.readIntLE();
        var workshopRate = buffer.readFloatLE();
        var unk1 = buffer.readIntLE();
        var transferable = buffer.readShortLE();
        var unk2 = buffer.readShortLE();
        var unk3 = buffer.readIntLE();
        var unk4 = buffer.readIntLE();

        return new IffClubSet(
                common,
                woodId,
                ironId,
                wedgeId,
                putterId,
                stats,
                workshopRate
        );
    }
}
