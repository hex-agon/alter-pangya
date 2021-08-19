package work.fking.pangya.game.packet.outbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.protocol.OutboundPacket;

public class MascotRosterPacket  implements OutboundPacket {

    private static final int ID = 0xe1;

    @Override
    public void encode(ByteBuf target) {
        target.writeShortLE(ID);

        target.writeShortLE(0); // count
    }
}
