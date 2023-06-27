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

        target.writeByte(2); // entry count

        target.writeByte(Course.BLUE_LAGOON.ordinal());
        target.writeIntLE(1000);

        target.writeByte(Course.ICE_SPA.ordinal());
        target.writeIntLE(1700);

        target.writeByte(Course.DEEP_INFERNO.ordinal());
        target.writeIntLE(1000);

        target.writeByte(Course.ABBOT_MINE.ordinal());
        target.writeIntLE(5000);
    }
}
