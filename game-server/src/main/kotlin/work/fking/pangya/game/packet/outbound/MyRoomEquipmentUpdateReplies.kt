package work.fking.pangya.game.packet.outbound

import io.netty.buffer.ByteBuf
import work.fking.pangya.networking.protocol.OutboundPacket

object MyRoomEquipmentUpdateReplies {

    enum class MyRoomEquipmentUpdateType(val id: Int) {
        CHARACTER_PARTS(0),
        CADDIE(1),
        EQUIPPED_ITEMS(2),
        COMET_CLUBSET(3),
        DECORATION(4),
        CHARACTER(5),
        MASCOT(8),
        CUT_IN(9)
    }

    enum class MyRoomEquipmentUpdateResult(val id: Int) {
        INCORRECT_ITEM_CODE(0),
        FAILED_BECAUSE_OF_DB_ERROR(1),
        SUCCESS(4),
        FAILED_TO_UPGRADE(5)
    }

    fun ack(result: MyRoomEquipmentUpdateResult, type: MyRoomEquipmentUpdateType, body: (buffer: ByteBuf) -> Unit = {}): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x6b)
            buffer.writeByte(result.id)
            buffer.writeByte(type.id)
            body(buffer)
        }
    }
}