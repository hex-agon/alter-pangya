package work.fking.pangya.game.model;

import io.netty.buffer.ByteBuf;

public class PangEquipment {

    public static PangEquipment mock() {
        return new PangEquipment();
    }

    public void encode(ByteBuf buffer) {
        buffer.writeIntLE(PangCaddie.mock().uniqueId());
        buffer.writeIntLE(PangCharacter.mock().uniqueId());
        buffer.writeIntLE(2000); // club, references an inventory unique id
        buffer.writeIntLE(335544325); // ball, iff id

        // items
        for (int i = 0; i < 10; i++) {
            buffer.writeIntLE(0);
        }
        buffer.writeIntLE(0); // background
        buffer.writeIntLE(0); // frame
        buffer.writeIntLE(0); // sticker
        buffer.writeIntLE(0); // slot
        buffer.writeIntLE(0);
        buffer.writeIntLE(0); // title
        buffer.writeIntLE(0); // skinBg
        buffer.writeIntLE(0); // skinFrame
        buffer.writeIntLE(0); // skinSticker
        buffer.writeIntLE(0); // skinSlot
        buffer.writeIntLE(0);
        buffer.writeIntLE(0); // title
        buffer.writeIntLE(0); // mascot
        buffer.writeIntLE(0); // poster1
        buffer.writeIntLE(0); // poster2
    }
}
