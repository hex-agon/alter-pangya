package work.fking.pangya.game.packet.outbound

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.room.RoomPlayer
import work.fking.pangya.networking.protocol.OutboundPacket

object RoomEquipmentUpdateReplies {
    enum class EquipmentUpdateType(val id: Int) {
        CADDIE(1),
        COMET(2),
        CLUBSET(3),
        CHARACTER(4),
        UNKNOWN(7)
    }

    fun ack(type: EquipmentUpdateType, player: RoomPlayer, body: (buffer: ByteBuf) -> Unit): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x4b)
            buffer.writeIntLE(0) // success/failed?
            buffer.writeByte(type.id)
            buffer.writeIntLE(player.connectionId)
            body(buffer)
        }
    }
}