package work.fking.pangya.game.packet.outbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.protocol.OutboundPacket;

public class Unknown305Packet implements OutboundPacket {

    private static final int ID = 305;
    private static final int[] VALUES = {750, 970, 990, 980, 980, 990, 900, 990, 990, 990, 990, 910, 1000, 740, 900, 980, 980, 0, 980, 940, 960};

    @Override
    public void encode(ByteBuf target) {
        target.writeShortLE(ID);
        target.writeByte(1);
        target.writeByte(15);

        for (int i = 0; i < VALUES.length; i++) {
            target.writeByte(i);
            target.writeIntLE(VALUES[i]);
        }
    }
}
