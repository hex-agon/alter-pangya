package work.fking.pangya.game.model;

import io.netty.buffer.ByteBuf;

public interface IffObject {

    int iffId();

    int uniqueId();

    void encode(ByteBuf buffer);
}
