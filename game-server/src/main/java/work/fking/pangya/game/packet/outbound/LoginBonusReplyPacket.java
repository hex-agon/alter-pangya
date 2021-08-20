package work.fking.pangya.game.packet.outbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.protocol.OutboundPacket;

public class LoginBonusReplyPacket implements OutboundPacket {

    private static final int ID = 0x0248;

    @Override
    public void encode(ByteBuf target) {
        target.writeShortLE(ID);

        target.writeZero(4); // padding
        target.writeByte(0); // bonusCollected
        target.writeIntLE(402653190); // item iffId
        target.writeIntLE(55); // quantity

        // if bonusCollected
        // target.writeIntLE(402653190); // future item iffId
        // target.writeIntLE(55); // future quantity
        // target.writeIntLE(5); // next bonus in n days
        // else
        target.writeZero(8); // padding
        target.writeIntLE(22); // current day streak
    }
}
