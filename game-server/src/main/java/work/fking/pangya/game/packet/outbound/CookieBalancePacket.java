package work.fking.pangya.game.packet.outbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.protocol.OutboundPacket;

public class CookieBalancePacket implements OutboundPacket {

    private static final int ID = 0x96;

    @Override
    public void encode(ByteBuf target) {
        target.writeShortLE(ID);

        target.writeIntLE(654321);
        target.writeIntLE(0);
    }
}
