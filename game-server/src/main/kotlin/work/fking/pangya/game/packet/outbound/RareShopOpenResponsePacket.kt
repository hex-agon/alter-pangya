package work.fking.pangya.game.packet.outbound

import io.netty.buffer.ByteBuf
import work.fking.pangya.networking.protocol.OutboundPacket

class RareShopOpenResponsePacket : OutboundPacket {

    override fun encode(target: ByteBuf) {
        target.writeShortLE(0x10B)
        target.writeIntLE(-0x1)
        target.writeIntLE(-0x1)
        target.writeIntLE(0)
    }
}
