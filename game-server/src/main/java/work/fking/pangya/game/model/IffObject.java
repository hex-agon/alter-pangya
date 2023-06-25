package work.fking.pangya.game.model;

import io.netty.buffer.ByteBuf;

public interface IffObject {

    int TYPE_CHARACTER = 4;
    int TYPE_PART = 8;
    int TYPE_CLUBSET = 16;
    int TYPE_BALL = 20;
    int TYPE_ITEM = 24;
    int TYPE_CADDIE = 28;

    int uid();

    int iffId();

    default int iffTypeId() {
        return iffTypeFromId(iffId());
    }

    void encode(ByteBuf buffer);

    static int iffTypeFromId(int iffId) {
        return iffId >> 24;
    }
}
