package work.fking.pangya.game.player;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.game.model.IffObject;

public record Caddie(
        int uid,
        int iffId,
        int levelsGained,
        int experience
) implements IffObject {

    public static Caddie mock() {
        return new Caddie(83651, 469762083, 2, 123);
    }

    @Override
    public void encode(ByteBuf buffer) {
        buffer.writeIntLE(uid);
        buffer.writeIntLE(iffId);
        buffer.writeIntLE(0);
        buffer.writeByte(levelsGained);
        buffer.writeIntLE(experience);
        buffer.writeLongLE(0);
    }
}
