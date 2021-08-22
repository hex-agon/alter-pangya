package work.fking.pangya.game.model;

import io.netty.buffer.ByteBuf;

public record PangCaddie(
        int iffId,
        int uniqueId
) implements IffUniqueObject {

    public static PangCaddie mock() {
        return new PangCaddie(469762083, 83651);
    }

    @Override
    public void encode(ByteBuf buffer) {
        buffer.writeIntLE(uniqueId);
        buffer.writeIntLE(iffId);
        buffer.writeIntLE(0);
        buffer.writeByte(0);
        buffer.writeIntLE(0);
        buffer.writeLongLE(0);
    }
}