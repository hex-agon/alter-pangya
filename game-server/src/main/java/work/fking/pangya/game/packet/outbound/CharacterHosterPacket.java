package work.fking.pangya.game.packet.outbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.protocol.OutboundPacket;

public class CharacterHosterPacket implements OutboundPacket {

    private static final int ID = 112;

    @Override
    public void encode(ByteBuf target) {
        target.writeShortLE(ID);

        target.writeShortLE(1); // count
        target.writeShortLE(1); // count

        // character entry
        target.writeIntLE(67108874);
        target.writeIntLE(0);
        target.writeIntLE(0);
        target.writeIntLE(0);

        for (int i = 0; i < 24; i++) {
            target.writeIntLE(0);
        }

        for (int i = 0; i < 24; i++) {
            target.writeIntLE(0);
        }
        target.writeBytes(new byte[216]);
        target.writeIntLE(0);
        target.writeIntLE(0);
        target.writeBytes(new byte[12]);
        target.writeIntLE(0);
        target.writeBytes(new byte[12]);
        target.writeByte(0); // power
        target.writeByte(0); // control
        target.writeByte(0); // impact
        target.writeByte(0); // spin
        target.writeByte(0); // curve
        target.writeByte(0); // mastery points
        target.writeMediumLE(0);

        // map cards
        for (int i = 0; i < 10; i++) {
            target.writeIntLE(0);
        }
        target.writeIntLE(0);
        target.writeIntLE(0);
    }
}
