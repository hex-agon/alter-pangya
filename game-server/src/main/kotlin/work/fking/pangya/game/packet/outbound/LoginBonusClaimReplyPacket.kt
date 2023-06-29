package work.fking.pangya.game.packet.outbound

import io.netty.buffer.ByteBuf
import work.fking.pangya.networking.protocol.OutboundPacket

class LoginBonusClaimReplyPacket : OutboundPacket {

    override fun encode(buffer: ByteBuf) {
        buffer.writeShortLE(0x249)
        buffer.writeZero(5) // padding
        buffer.writeIntLE(402653190) // item iffId
        buffer.writeIntLE(55) // quantity
        buffer.writeIntLE(402653190) // future item iffId
        buffer.writeIntLE(40) // future quantity
        buffer.writeIntLE(22) // current day streak
    }
}
