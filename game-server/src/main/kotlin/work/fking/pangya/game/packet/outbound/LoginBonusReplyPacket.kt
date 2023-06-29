package work.fking.pangya.game.packet.outbound

import io.netty.buffer.ByteBuf
import work.fking.pangya.networking.protocol.OutboundPacket

class LoginBonusReplyPacket : OutboundPacket {

    override fun encode(target: ByteBuf) {
        target.writeShortLE(0x248)
        target.writeZero(4) // padding
        target.writeByte(0) // bonusCollected
        target.writeIntLE(402653190) // item iffId
        target.writeIntLE(55) // quantity

        // if bonusCollected
        // target.writeIntLE(402653190); // future item iffId
        // target.writeIntLE(55); // future quantity
        // target.writeIntLE(5); // next bonus in n days
        // else
        target.writeZero(8) // padding
        target.writeIntLE(22) // current day streak
    }
}
