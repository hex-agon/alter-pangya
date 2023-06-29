package work.fking.pangya.login.packet.outbound

import io.netty.buffer.ByteBuf
import work.fking.pangya.networking.protocol.OutboundPacket
import work.fking.pangya.networking.protocol.writeFixedSizeString
import work.fking.pangya.networking.protocol.writePString

object LoginReplies {
    private const val RESULT_PACKET_ID = 0x1
    private const val SESSION_KEY_PACKET_ID = 0x3
    private const val CHAT_MACROS_PACKET_ID = 0x6
    private const val LOGIN_KEY_PACKET_ID = 0x10

    fun loginKey(loginKey: String): OutboundPacket {
        return OutboundPacket { buffer: ByteBuf ->
            buffer.writeShortLE(LOGIN_KEY_PACKET_ID)
            buffer.writePString(loginKey)
        }
    }

    fun sessionKey(sessionKey: String): OutboundPacket {
        return OutboundPacket { buffer: ByteBuf ->
            buffer.writeShortLE(SESSION_KEY_PACKET_ID)
            buffer.writeZero(4)
            buffer.writePString(sessionKey)
        }
    }

    fun chatMacros(): OutboundPacket {
        return OutboundPacket { buffer: ByteBuf ->
            buffer.writeShortLE(CHAT_MACROS_PACKET_ID)
            for (i in 0..8) {
                buffer.writeFixedSizeString("Welcome to PangYa!", 64)
            }
        }
    }

    fun success(uid: Int, username: String, nickname: String): OutboundPacket {
        return OutboundPacket { buffer: ByteBuf ->
            buffer.writeShortLE(RESULT_PACKET_ID)
            buffer.writeByte(0) // success
            buffer.writePString(username)
            buffer.writeIntLE(uid)
            buffer.writeZero(14)
            buffer.writePString(nickname)
        }
    }

    fun error(error: Error, message: String = ""): OutboundPacket {
        return OutboundPacket { buffer: ByteBuf ->
            buffer.writeShortLE(RESULT_PACKET_ID)
            buffer.writeByte(error.code)
            buffer.writePString(message)
        }
    }

    fun accountSuspended(liftTimeHours: Int): OutboundPacket {
        return OutboundPacket { buffer: ByteBuf ->
            buffer.writeShortLE(RESULT_PACKET_ID)
            buffer.writeByte(0x7)
            buffer.writeIntLE(liftTimeHours)
        }
    }

    fun selectCharacter(): OutboundPacket {
        return OutboundPacket { buffer: ByteBuf ->
            buffer.writeShortLE(RESULT_PACKET_ID)
            buffer.writeByte(0xD9)
        }
    }

    fun createNickname(): OutboundPacket {
        return OutboundPacket { buffer: ByteBuf ->
            buffer.writeShortLE(RESULT_PACKET_ID)
            buffer.writeByte(0xD8)
        }
    }

    enum class Error(val code: Int) {
        INVALID_ID_PW(0x1),
        INVALID_ID(0x2),
        USERNAME_IN_USE(0x4),
        BANNED(0x5),
        INCORRECT_USERNAME_PASSWORD(0x6),
        PLAYER_UNDER_AGE(0x9),
        INCORRECT_SSN(0xC),
        INCORRECT_USERNAME(0xD),
        WHITELISTED_USERS_ONLY(0xE),
        SERVER_MAINTENANCE(0xF),
        GEO_BLOCKED(0x10)
    }
}
