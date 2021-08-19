package work.fking.pangya.game.packet.outbound;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import work.fking.pangya.networking.protocol.OutboundPacket;

public class EquipmentPacket implements OutboundPacket {

    private static final int ID = 0x72;

    @Override
    public void encode(ByteBuf target) {
        target.writeShortLE(ID);

        target.writeIntLE(83651); // caddie
        target.writeIntLE(262513); // character
        target.writeIntLE(15069162); // club
        target.writeIntLE(335544325); // ball

        // items
        for (int i = 0; i < 10; i++) {
            target.writeIntLE(0);
        }
        target.writeIntLE(0); // background
        target.writeIntLE(0); // frame
        target.writeIntLE(0); // sticker
        target.writeIntLE(0); // slot
        target.writeIntLE(0);
        target.writeIntLE(0); // title
        target.writeIntLE(0); // skinBg
        target.writeIntLE(0); // skinFrame
        target.writeIntLE(0); // skinSticker
        target.writeIntLE(0); // skinSlot
        target.writeIntLE(0);
        target.writeIntLE(0); // title
        target.writeIntLE(0); // mascot
        target.writeIntLE(0); // poster1
        target.writeIntLE(0); // poster2
    }
}
