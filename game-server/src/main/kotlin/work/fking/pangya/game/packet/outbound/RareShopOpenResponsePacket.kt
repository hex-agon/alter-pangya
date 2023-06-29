package work.fking.pangya.game.packet.outbound

import io.netty.buffer.ByteBuf
import work.fking.pangya.networking.protocol.OutboundPacket

class RareShopOpenResponsePacket : OutboundPacket {

    override fun encode(buffer: ByteBuf) {
        buffer.writeShortLE(0x10B)
        buffer.writeIntLE(-0x1)
        buffer.writeIntLE(-0x1)
        buffer.writeIntLE(0)
    }
}
