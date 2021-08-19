package work.fking.pangya.game.packet.outbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.protocol.OutboundPacket;

public class CaddieHosterPacket implements OutboundPacket {

    private static final int ID = 0x71;

    @Override
    public void encode(ByteBuf target) {
        target.writeShortLE(ID);

        target.writeShortLE(1); // count
        target.writeShortLE(1); // pageCount

        // caddie
        target.writeIntLE(83651); // this is like a pk id, this is used to reference on the equip id packet
        target.writeIntLE(469762072); // iffId
        target.writeIntLE(0); // unknown
        target.writeByte(0);
        target.writeIntLE(0); // exp?
        target.writeLongLE(0); // unknown
    }
}
