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
        target.writeIntLE(469762055); // iff id
        target.writeIntLE(0); // slot
        target.writeIntLE(0); // hair color
        target.writeIntLE(0);

        // parts
        target.writeIntLE(136839171);
        target.writeIntLE(136855557);
        target.writeIntLE(136871940);
        target.writeIntLE(136880130);
        target.writeIntLE(136896516);

        for (int i = 0; i < 24 - 5; i++) {
            target.writeIntLE(0);
        }

        target.writeIntLE(0);
        target.writeIntLE(1);
        target.writeIntLE(2);
        target.writeIntLE(3);
        target.writeIntLE(4);

        for (int i = 0; i < 24 - 5; i++) {
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
