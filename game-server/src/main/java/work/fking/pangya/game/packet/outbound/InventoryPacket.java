package work.fking.pangya.game.packet.outbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.protocol.OutboundPacket;

public class InventoryPacket implements OutboundPacket {

    private static final int ID = 0x73;

    @Override
    public void encode(ByteBuf target) {
        target.writeShortLE(ID);

        target.writeShortLE(14);
        target.writeShortLE(14);

        // for each item in this packet...
        target.writeIntLE(1000); // item unique id
        target.writeIntLE(335544325); // item iff id
        target.writeIntLE(0); // unknown
        target.writeIntLE(200); // item quantity
        target.writeZero(0xb4);

        target.writeIntLE(2000); // item unique id
        target.writeIntLE(268435511); // item iff id
        target.writeIntLE(0); // unknown
        target.writeIntLE(0); // item quantity
        target.writeZero(0xb4);

        target.writeIntLE(3000); // item unique id
        target.writeIntLE(436207656); // item iff id
        target.writeIntLE(0); // unknown
        target.writeIntLE(200); // item quantity
        target.writeZero(0xb4);

        // lucia test items
        int uniqueId = 7000;
        target.writeIntLE(uniqueId++); // item unique id
        target.writeIntLE(136331265); // item iff id
        target.writeIntLE(0); // unknown
        target.writeIntLE(0); // item quantity
        target.writeZero(0xb4);

        target.writeIntLE(uniqueId++); // item unique id
        target.writeIntLE(136423465); // item iff id
        target.writeIntLE(0); // unknown
        target.writeIntLE(0); // item quantity
        target.writeZero(0xb4);

        target.writeIntLE(uniqueId++); // item unique id
        target.writeIntLE(136445968); // item iff id
        target.writeIntLE(0); // unknown
        target.writeIntLE(0); // item quantity
        target.writeZero(0xb4);

        target.writeIntLE(uniqueId++); // item unique id
        target.writeIntLE(136456205); // item iff id
        target.writeIntLE(0); // unknown
        target.writeIntLE(0); // item quantity
        target.writeZero(0xb4);

        target.writeIntLE(uniqueId++); // item unique id
        target.writeIntLE(136456192); // item iff id
        target.writeIntLE(0); // unknown
        target.writeIntLE(0); // item quantity
        target.writeZero(0xb4);

        target.writeIntLE(uniqueId++); // item unique id
        target.writeIntLE(136398858); // item iff id
        target.writeIntLE(0); // unknown
        target.writeIntLE(0); // item quantity
        target.writeZero(0xb4);

        target.writeIntLE(uniqueId++); // item unique id
        target.writeIntLE(136372302); // item iff id
        target.writeIntLE(0); // unknown
        target.writeIntLE(0); // item quantity
        target.writeZero(0xb4);

        target.writeIntLE(uniqueId++); // item unique id
        target.writeIntLE(136355870); // item iff id
        target.writeIntLE(0); // unknown
        target.writeIntLE(0); // item quantity
        target.writeZero(0xb4);

        target.writeIntLE(uniqueId++); // item unique id
        target.writeIntLE(136347689); // item iff id
        target.writeIntLE(0); // unknown
        target.writeIntLE(0); // item quantity
        target.writeZero(0xb4);

        target.writeIntLE(uniqueId++); // item unique id
        target.writeIntLE(136331362); // item iff id
        target.writeIntLE(0); // unknown
        target.writeIntLE(0); // item quantity
        target.writeZero(0xb4);
    }
}
