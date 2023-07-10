package work.fking.pangya.game.packet.outbound

import work.fking.pangya.game.player.Player
import work.fking.pangya.game.player.PlayerBasicInfo
import work.fking.pangya.game.player.PlayerStatistic
import work.fking.pangya.game.player.PlayerTrophies
import work.fking.pangya.game.player.write
import work.fking.pangya.game.room.Course
import work.fking.pangya.game.room.CourseStatistics
import work.fking.pangya.networking.protocol.OutboundPacket
import work.fking.pangya.networking.protocol.writeFixedSizeString
import work.fking.pangya.networking.protocol.writeLocalDateTime
import work.fking.pangya.networking.protocol.writePString
import java.time.LocalDateTime

object HandoverReplies {
    private const val PACKET_ID = 0x44
    private const val TYPE_PROGRESS_BAR = 0xD2
    private const val TYPE_LOGIN_OK = 0xD3

    fun ok(): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(PACKET_ID)
            buffer.writeShortLE(TYPE_LOGIN_OK)
            buffer.writeByte(0)
        }
    }

    fun error(result: HandoverResult): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(PACKET_ID)
            buffer.writeShortLE(result.code)
        }
    }

    /**
     * @param value Accepted values are between 0 and 15 inclusive
     */
    fun updateProgressBar(value: Int): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(PACKET_ID)
            buffer.writeByte(TYPE_PROGRESS_BAR)
            buffer.writeByte(value)
        }
    }

    fun handoverReply(player: Player): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(PACKET_ID)
            buffer.writeByte(0) // sub packet type

            // Server Info
            buffer.writePString("852.00") // server version
            buffer.writePString("alter-pangya") // server name

            // User info
            PlayerBasicInfo().encode(buffer, player)

            // Player Statistics (verified ok by comparing with struct sent on hole end)
            buffer.write(PlayerStatistic())

            // trophies (verified ok by checking my room trophies and confirming there's 1 ama 6 gold trophy and 1 bronze pro 7 trophy)
            PlayerTrophies().encode(buffer)

            // Player equipment
            player.equipment.encode(buffer)

            // Season historical stats
            repeat(12) { // for each season...
                repeat(21) { // for each course...
                    CourseStatistics(Course.BLUE_LAGOON).serialize(buffer)
                }
            }
            // Active Character
            player.equippedCharacter().encode(buffer)

            // Active Caddie
            player.equippedCaddie().encode(buffer)

            // Active Clubset (seemingly completely ignored by the client)
            val clubSet = player.equippedClubSet() ?: throw IllegalStateException("player doesn't have a clubset equipped")
            buffer.writeIntLE(clubSet.uid)
            buffer.writeIntLE(clubSet.iffId)
            repeat(5) { buffer.writeShortLE(0) }
            repeat(5) { buffer.writeShortLE(0) }

            // Active Mascot
            buffer.writeIntLE(0) // item unique id
            buffer.writeIntLE(0) // item iff id
            buffer.writeByte(0) // level
            buffer.writeIntLE(0) // experience
            buffer.writeFixedSizeString("0", 16)
            buffer.writeZero(33)

            // Server Time
            buffer.writeLocalDateTime(LocalDateTime.now())

            buffer.writeShortLE(0)
            // Papel shop info?
            buffer.writeShortLE(0)
            buffer.writeShortLE(0)
            buffer.writeShortLE(0)

            buffer.writeIntLE(0) // unknown

            // known ok
            buffer.writeLongLE((1 shl 18).toLong()) // disabled server features
            buffer.writeIntLE(0) // unknown, ss = login count
            buffer.writeIntLE(0) // ss = server flags

            // guild info
            buffer.writeZero(277)
        }
    }

    enum class HandoverResult(val code: Int) {
        // 1, 9, 10, 12, 13 = causes the client to send select server packet & reconnect to the login server, probably used if the login key doesn't match
        // 3 = cannot connect to the login server
        // 5 = this id has been permanently blocked, contact support...
        // 7 = this id has been blocked, contact support...
        // 9 = reconnects too
        // 11 = Server version miss match
        // 14, 15 = only certain allowed users may join this server.
        // 16, 17 = pangya is not available in your area
        // 19...31 = Account has been transferred
        RECONNECT_LOGIN_SERVER(1),
        CANNOT_CONNECT_LOGIN_SERVER(3),
        ID_PERMANENTLY_BLOCKED(5),
        ID_BLOCKED(7),
        SERVER_VERSION_MISSMATCH(11),
        NON_WHITELISTED_USER(14),
        GEO_BLOCKED(16),
        ACCOUNT_TRANSFERRED(19)
    }
}
