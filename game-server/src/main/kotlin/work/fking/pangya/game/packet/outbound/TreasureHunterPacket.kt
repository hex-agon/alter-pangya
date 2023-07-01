package work.fking.pangya.game.packet.outbound

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.room.Course
import work.fking.pangya.networking.protocol.OutboundPacket

class TreasureHunterPacket : OutboundPacket {

    override fun encode(buffer: ByteBuf) {
        buffer.writeShortLE(0x131)
        buffer.writeByte(1)
        buffer.writeByte(1) // entry count

        buffer.writeByte(Course.BLUE_LAGOON.ordinal)
        buffer.writeIntLE(900)
    }
}
