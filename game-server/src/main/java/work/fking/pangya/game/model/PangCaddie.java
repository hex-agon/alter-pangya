package work.fking.pangya.game.model;

import io.netty.buffer.ByteBuf;

public record PangCaddie(
        int iffId,
        int uniqueId,
        int levelsGained,
        int experience
) implements IffObject {

    public static PangCaddie mock() {
        return new PangCaddie(469762083, 83651, 2, 123);
    }

    @Override
    public void encode(ByteBuf buffer) {
        buffer.writeIntLE(uniqueId);
        buffer.writeIntLE(iffId);
        buffer.writeIntLE(0);
        buffer.writeByte(levelsGained);
        buffer.writeIntLE(experience);
        buffer.writeLongLE(0);
    }
}
