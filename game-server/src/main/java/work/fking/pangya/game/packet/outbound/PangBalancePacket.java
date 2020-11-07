package work.fking.pangya.game.packet.outbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.protocol.OutboundPacket;

public class PangBalancePacket implements OutboundPacket {

    private static final int ID = 200;

    @Override
    public void encode(ByteBuf target) {
        target.writeShortLE(ID);
        target.writeIntLE(123456);
        target.writeIntLE(0);

    }
}
