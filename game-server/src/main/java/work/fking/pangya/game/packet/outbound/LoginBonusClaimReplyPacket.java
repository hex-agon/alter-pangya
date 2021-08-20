package work.fking.pangya.game.packet.outbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.protocol.OutboundPacket;

public class LoginBonusClaimReplyPacket implements OutboundPacket {

    private static final int ID = 0x0249;

    @Override
    public void encode(ByteBuf target) {
        target.writeShortLE(ID);

        target.writeZero(5); // padding
        target.writeIntLE(402653190); // item iffId
        target.writeIntLE(55); // quantity

        target.writeIntLE(402653190); // future item iffId
        target.writeIntLE(40); // future quantity

        target.writeIntLE(22); // current day streak
    }
}
