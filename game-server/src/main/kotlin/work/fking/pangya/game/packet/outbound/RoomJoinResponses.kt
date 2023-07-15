package work.fking.pangya.game.packet.outbound

import work.fking.pangya.game.room.RoomJoinError
import work.fking.pangya.networking.protocol.OutboundPacket
import work.fking.pangya.networking.protocol.writePString

object RoomJoinResponses {
    private const val ID = 0x49
    private const val SUCCESS = 0

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

    fun error(joinError: RoomJoinError): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(ID)
            buffer.writeByte(joinError.value)
        }
    }
}

