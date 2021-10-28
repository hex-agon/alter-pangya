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
) implements IffObject {

    public static PangCharacter mock() {
        return new PangCharacter(
                67108872,
                262513,
                2,
                new int[] {
                        0, 0, 136331362, 136340480, 136347689, 0, 136364603, 136372302, 0, 0, 136398858, 0, 0, 136423465, 0, 0, 136445968, 136456205, 0, 0, 0, 0, 0, 0
                },
                new int[] {
                        0, 0, 7009, 0, 7008, 0, 7006, 7006, 0, 0, 7005, 0, 0, 7001, 0, 0, 7002, 7003, 0, 0, 0, 0, 0, 0
                },
                new int[] {8, 5, 4, 2, 1},
                0,
                new int[10]
        );
    }

    public int stat(Stat stat) {
        return stats[stat.ordinal()];
    }

    public static PangCharacter decode(ByteBuf buffer) {
        var iffId = buffer.readIntLE();
        var uniqueId = buffer.readIntLE();
        var hairColor = buffer.readIntLE();

        var equipmentIffIds = new int[24];
        for (int i = 0; i < equipmentIffIds.length; i++) {
            equipmentIffIds[i] = buffer.readIntLE();
        }
        var equipmentUniqueIds = new int[24];
        for (int i = 0; i < equipmentUniqueIds.length; i++) {
            equipmentUniqueIds[i] = buffer.readIntLE();
        }
        buffer.skipBytes(216);
        buffer.skipBytes(4);
        buffer.skipBytes(4);
        buffer.skipBytes(12);
        buffer.skipBytes(4);
        buffer.skipBytes(12);

        var stats = new int[5];
        for (int i = 0; i < stats.length; i++) {
            stats[i] = buffer.readByte();
        }
        var masteryPoints = buffer.readByte();
        buffer.skipBytes(3);

        var cardIffIds = new int[10];
        for (int i = 0; i < cardIffIds.length; i++) {
            cardIffIds[i] = buffer.readIntLE();
        }
        buffer.skipBytes(8);

        return new PangCharacter(
                iffId,
                uniqueId,
                hairColor,
                equipmentIffIds,
                equipmentUniqueIds,
                stats,
                masteryPoints,
                cardIffIds
        );
    }

    @Override
    public void encode(ByteBuf buffer) {
        buffer.writeIntLE(iffId);
        buffer.writeIntLE(uniqueId);
        buffer.writeIntLE(hairColor);

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
        buffer.writeZero(3);

        for (int cardIffId : cardIffIds) {
            buffer.writeIntLE(cardIffId);
        }
        buffer.writeIntLE(0);
        buffer.writeIntLE(0);
    }
}
