package work.fking.pangya.game.packet.outbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.protocol.OutboundPacket;

public class InventoryPacket implements OutboundPacket {

    private static final int ID = 115;

    @Override
    public void encode(ByteBuf target) {
        target.writeShortLE(ID);

        target.writeShortLE(0);
        target.writeShortLE(0);
    }
}
