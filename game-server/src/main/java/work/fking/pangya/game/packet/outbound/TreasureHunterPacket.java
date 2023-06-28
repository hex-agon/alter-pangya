package work.fking.pangya.game.packet.outbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.game.model.Course;
import work.fking.pangya.networking.protocol.OutboundPacket;

public class TreasureHunterPacket implements OutboundPacket {

    private static final int ID = 0x131;

    @Override
    public void encode(ByteBuf target) {
        target.writeShortLE(ID);
        target.writeByte(1);

        target.writeByte(1); // entry count

        target.writeByte(Course.BLUE_LAGOON.ordinal());
        target.writeIntLE(900);

    }
}
