package work.fking.pangya.game.packet.outbound

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.model.Course
import work.fking.pangya.networking.protocol.OutboundPacket

class TreasureHunterPacket : OutboundPacket {

    override fun encode(target: ByteBuf) {
        target.writeShortLE(0x131)
        target.writeByte(1)
        target.writeByte(1) // entry count

        target.writeByte(Course.BLUE_LAGOON.ordinal)
        target.writeIntLE(900)
    }
}
