package work.fking.pangya.game.packet.outbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.protocol.OutboundPacket;

public class InventoryPacket implements OutboundPacket {

    private static final int ID = 0x73;

    @Override
    public void encode(ByteBuf target) {
        target.writeShortLE(ID);

        target.writeShortLE(3);
        target.writeShortLE(3);

        // for each item in this packet...
        target.writeIntLE(1000); // item unique id
        target.writeIntLE(335544325); // item iff id
        target.writeIntLE(0); // unknown
        target.writeIntLE(200); // item quantity
        target.writeBytes(new byte[0xb4]);

        target.writeIntLE(2000); // item unique id
        target.writeIntLE(268435511); // item iff id
        target.writeIntLE(0); // unknown
        target.writeIntLE(0); // item quantity
        target.writeBytes(new byte[0xb4]);

        target.writeIntLE(7000); // item unique id
        target.writeIntLE(136331265); // item iff id
        target.writeIntLE(0); // unknown
        target.writeIntLE(0); // item quantity
        target.writeBytes(new byte[0xb4]);
    }
}
