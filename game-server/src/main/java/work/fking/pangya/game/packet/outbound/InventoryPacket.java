package work.fking.pangya.game.packet.outbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.protocol.OutboundPacket;

public class InventoryPacket implements OutboundPacket {

    private static final int ID = 115;

    @Override
    public void encode(ByteBuf target) {
        target.writeShortLE(ID);

        target.writeShortLE(1);
        target.writeShortLE(1);

        // for each item in this packet...
        target.writeIntLE(0); // item slot
        target.writeIntLE(402653188); // item iff id
        target.writeIntLE(0); // unknown
        target.writeIntLE(200); // item quantity
        target.writeBytes(new byte[0xb4]);
    }
}
