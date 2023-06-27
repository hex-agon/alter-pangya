package work.fking.pangya.game.player;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.game.model.IffObject;
import work.fking.pangya.game.model.Stat;

public record Character(
        int uid,
        int iffId,
        int hairColor,
        int[] partIffIds,
        int[] partUids,
        int[] stats,
        int masteryPoints,
        int[] cardIffIds
) implements IffObject {

    private static final int PARTS = 24;
    private static final int STATS = 5;
    private static final int CARDS = 10;

    public int stat(Stat stat) {
        return stats[stat.ordinal()];
    }

    public static Character decode(ByteBuf buffer) {
        var iffId = buffer.readIntLE();
        var uid = buffer.readIntLE();
        var hairColor = buffer.readIntLE();

        var partIffIds = new int[PARTS];
        for (int i = 0; i < PARTS; i++) {
            partIffIds[i] = buffer.readIntLE();
        }
        var partUids = new int[PARTS];
        for (int i = 0; i < PARTS; i++) {
            partUids[i] = buffer.readIntLE();
        }
        buffer.skipBytes(216);
        buffer.skipBytes(4);
        buffer.skipBytes(4);
        buffer.skipBytes(12);
        buffer.skipBytes(4);
        buffer.skipBytes(12);

        var stats = new int[STATS];
        for (int i = 0; i < stats.length; i++) {
            stats[i] = buffer.readByte();
        }
        var masteryPoints = buffer.readByte();
        buffer.skipBytes(3);

        var cardIffIds = new int[CARDS];
        for (int i = 0; i < cardIffIds.length; i++) {
            cardIffIds[i] = buffer.readIntLE();
        }
        buffer.skipBytes(8);

        return new Character(
                uid,
                iffId,
                hairColor,
                partIffIds,
                partUids,
                stats,
                masteryPoints,
                cardIffIds
        );
    }

    @Override
    public void encode(ByteBuf buffer) {
        buffer.writeIntLE(iffId);
        buffer.writeIntLE(uid);
        buffer.writeIntLE(hairColor);

        for (int iffId : partIffIds) {
            buffer.writeIntLE(iffId);
        }
        for (int uniqueId : partUids) {
            buffer.writeIntLE(uniqueId);
        }
        buffer.writeZero(216);
        buffer.writeIntLE(0);
        buffer.writeIntLE(0);
        buffer.writeZero(12);
        buffer.writeIntLE(0);
        buffer.writeZero(12);
        buffer.writeByte(stat(Stat.POWER));
        buffer.writeByte(stat(Stat.CONTROL));
        buffer.writeByte(stat(Stat.ACCURACY));
        buffer.writeByte(stat(Stat.SPIN));
        buffer.writeByte(stat(Stat.CURVE));
        buffer.writeByte(masteryPoints);
        buffer.writeZero(3);

        for (int cardIffId : cardIffIds) {
            buffer.writeIntLE(cardIffId);
        }
        buffer.writeIntLE(0);
        buffer.writeIntLE(0);
    }
}
