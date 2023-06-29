package work.fking.pangya.game.packet.outbound

import work.fking.pangya.networking.protocol.OutboundPacket
import work.fking.pangya.networking.protocol.writePString

object RoomJoinResponses {
    private const val ID = 0x49
    private const val SUCCESS = 0
    private const val ALREADY_STARTED = 8
    private const val CANNOT_CREATE = 18

    fun success(name: String, number: Int): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(ID)
            buffer.writeByte(SUCCESS)
            buffer.writeZero(1)
            buffer.writePString(name)
            buffer.writeZero(25)
            buffer.writeShortLE(number)
            buffer.writeZero(111)
            buffer.writeIntLE(0) // eventNumber
            buffer.writeZero(12)
        }
    }

    fun alreadyStarted(): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(ID)
            buffer.writeByte(ALREADY_STARTED)
        }
    }

    fun cannotCreate(): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(ID)
            buffer.writeByte(CANNOT_CREATE)
        }
    }
}
