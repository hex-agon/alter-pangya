package work.fking.pangya.common.model.player;

import io.netty.buffer.ByteBuf;

public record PlayerCharacter(int id, int hairColor) {

    private static final byte[] UNKNOWN = new byte[503];

    public void encode(ByteBuf buffer) {
        buffer.writeIntLE(id);
        buffer.writeIntLE(0); // uniqueId
        buffer.writeByte(hairColor);
        buffer.writeBytes(UNKNOWN);
    }
}
