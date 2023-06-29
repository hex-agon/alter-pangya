package work.fking.pangya.game.packet.outbound

import io.netty.buffer.ByteBuf
import work.fking.pangya.networking.protocol.OutboundPacket

class LoginBonusReplyPacket : OutboundPacket {

    override fun encode(buffer: ByteBuf) {
        buffer.writeShortLE(0x248)
        buffer.writeZero(4) // padding
        buffer.writeByte(0) // bonusCollected
        buffer.writeIntLE(402653190) // item iffId
        buffer.writeIntLE(55) // quantity

        // if bonusCollected
        // target.writeIntLE(402653190); // future item iffId
        // target.writeIntLE(55); // future quantity
        // target.writeIntLE(5); // next bonus in n days
        // else
        buffer.writeZero(8) // padding
        buffer.writeIntLE(22) // current day streak
    }
}
