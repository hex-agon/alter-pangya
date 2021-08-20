package work.fking.pangya.game.packet.outbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.protocol.OutboundPacket;

public class CharacterHosterPacket implements OutboundPacket {

    private static final int ID = 0x70;

    @Override
    public void encode(ByteBuf target) {
        target.writeShortLE(ID);

        target.writeShortLE(1); // count
        target.writeShortLE(1); // count

        // character entry
        target.writeIntLE(67108872); // iff id
        target.writeIntLE(262513); // this is like a pk id, this is used to reference on the equip id packet
        target.writeIntLE(3); // hair color
        target.writeIntLE(0);

        // parts iffIds
        int[] partIffIds = {
                0, 0, 136331358, 136339993, 136347688, 0,
                136364599, 136372298, 0, 0, 136398848, 136407055,
                0, 136423466, 0, 136439863, 136445997, 136456205,
                0, 0, 0, 0, 0, 0
        };
        for (int partIffId : partIffIds) {
            target.writeIntLE(partIffId);
        }
        // end parts iffIds

        // parts unique ids
        int[] partUniqueIds = {
                0, 0, 15069258, 0, 15069259, 0,
                0, 15069260, 0, 0, 15069792, 15071153,
                0, 15070499, 0, 15069784, 15069816, 15075062,
                0, 0, 0, 0, 0, 0
        };
        for (int partUniqueId : partUniqueIds) {
            target.writeIntLE(partUniqueId);
        }
        // end parts unique ids

        target.writeZero(216);
        target.writeIntLE(0);
        target.writeIntLE(0);
        target.writeZero(12);
        target.writeIntLE(0);
        target.writeZero(12);
        target.writeByte(8); // power
        target.writeByte(5); // control
        target.writeByte(5); // accuracy
        target.writeByte(2); // spin
        target.writeByte(1); // curve
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
