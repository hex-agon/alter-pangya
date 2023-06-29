package work.fking.pangya.game.packet.outbound

import io.netty.buffer.ByteBuf
import work.fking.pangya.networking.protocol.OutboundPacket

class MascotRosterPacket : OutboundPacket {

    override fun encode(target: ByteBuf) {
        target.writeShortLE(0xe1)
        target.writeShortLE(0) // count
    }
}
