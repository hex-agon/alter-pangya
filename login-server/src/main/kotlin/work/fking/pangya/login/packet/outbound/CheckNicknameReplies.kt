package work.fking.pangya.login.packet.outbound

import work.fking.pangya.networking.protocol.OutboundPacket
import work.fking.pangya.networking.protocol.writePString

object CheckNicknameReplies {
    private const val PACKET_ID = 0xE

    fun available(nickname: String): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(PACKET_ID)
            buffer.writeIntLE(0)
            buffer.writePString(nickname)
        }
    }

    fun error(error: Error): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(PACKET_ID)
            buffer.writeIntLE(error.code)
        }
    }

    enum class Error(val code: Int) {
        ERROR(1),
        IN_USE(3),
        INCORRECT_FORMAT_OR_LENGTH(4),
        NOT_ENOUGH_POINTS(5),
        INAPPROPRIATE_WORDS(6),
        DATABASE_ERROR(7),
        SAME_NICKNAME_WILL_BE_USED(9)
    }
}
