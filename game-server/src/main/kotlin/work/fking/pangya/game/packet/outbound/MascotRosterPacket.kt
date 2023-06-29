package work.fking.pangya.game.packet.outbound

import io.netty.buffer.ByteBuf
import work.fking.pangya.networking.protocol.OutboundPacket

class MascotRosterPacket : OutboundPacket {

    override fun encode(buffer: ByteBuf) {
        buffer.writeShortLE(0xe1)
        buffer.writeShortLE(0) // count
    }
}
