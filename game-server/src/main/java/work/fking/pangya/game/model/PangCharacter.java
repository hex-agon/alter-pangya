package work.fking.pangya.game.model;

import io.netty.buffer.ByteBuf;

public record PangCharacter(
        int iffId,
        int uniqueId,
        int hairColor,
        int[] equipmentIffIds,
        int[] equipmentUniqueIds,
        int[] stats,
        int masteryPoints,
        int[] cardIffIds
) implements IffUniqueObject {

    public static PangCharacter mock() {
        return new PangCharacter(
                67108872,
                262513,
                2,
                new int[] {
                        0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0
                },
                new int[] {
                        0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0
                },
                new int[] {8, 5, 4, 2, 1},
                0,
                new int[10]
        );
    }

    public int stat(Stat stat) {
        return stats[stat.ordinal()];
    }

    @Override
    public void encode(ByteBuf buffer) {
        buffer.writeIntLE(iffId);
        buffer.writeIntLE(uniqueId);
        buffer.writeIntLE(hairColor);
        buffer.writeIntLE(0);

        for (int iffId : equipmentIffIds) {
            buffer.writeIntLE(iffId);
        }
        for (int uniqueId : equipmentUniqueIds) {
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
        buffer.writeMediumLE(0);

        for (int cardIffId : cardIffIds) {
            buffer.writeIntLE(cardIffId);
        }
        buffer.writeLongLE(0);
    }
}
