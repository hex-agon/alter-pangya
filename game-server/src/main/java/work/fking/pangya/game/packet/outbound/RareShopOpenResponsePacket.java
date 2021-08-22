package work.fking.pangya.game.packet.outbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.protocol.OutboundPacket;

public class RareShopOpenResponsePacket implements OutboundPacket {

    private static final int ID = 0x010B;

    @Override
    public void encode(ByteBuf target) {
        target.writeShortLE(ID);
        target.writeIntLE(0xFFFFFFFF);
        target.writeIntLE(0xFFFFFFFF);
        target.writeIntLE(0);
    }
}
