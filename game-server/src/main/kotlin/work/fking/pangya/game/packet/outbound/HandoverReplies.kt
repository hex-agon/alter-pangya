package work.fking.pangya.game.packet.outbound

import work.fking.pangya.game.model.CourseStatistics
import work.fking.pangya.game.player.Caddie
import work.fking.pangya.game.player.Player
import work.fking.pangya.game.player.PlayerBasicInfo
import work.fking.pangya.game.player.PlayerStatistic
import work.fking.pangya.game.player.PlayerTrophies
import work.fking.pangya.game.room.Course
import work.fking.pangya.networking.protocol.OutboundPacket
import work.fking.pangya.networking.protocol.writeFixedSizeString
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

            // Player Statistics
            PlayerStatistic().encode(buffer)

            // trophies
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
            val caddie = player.activeCaddie() ?: Caddie.nullCaddie()
            caddie.encode(buffer)

            // Active Clubset
            buffer.writeIntLE(player.inventory.findByIffId(268435511)!!.uid) // item unique id
            buffer.writeIntLE(268435511) // item iff id
            buffer.writeZero(10)
            buffer.writeShortLE(0) // power upgrades?
            buffer.writeShortLE(0) // control upgrades?
            buffer.writeShortLE(0) // accuracy upgrades?
            buffer.writeShortLE(0) // spin upgrades?
            buffer.writeShortLE(0) // curve upgrades?

            // Active Mascot
            buffer.writeIntLE(0) // item unique id
            buffer.writeIntLE(0) // item iff id
            buffer.writeByte(0) // level
            buffer.writeIntLE(0) // experience
            buffer.writeFixedSizeString("mascot1", 16)
            buffer.writeZero(33)

            // Server Time
            val now = LocalDateTime.now()
            buffer.writeShortLE(now.year)
            buffer.writeShortLE(now.monthValue)
            buffer.writeShortLE(now.dayOfWeek.value)
            buffer.writeShortLE(now.dayOfMonth)
            buffer.writeShortLE(now.hour)
            buffer.writeShortLE(now.minute)
            buffer.writeShortLE(now.second)
            buffer.writeShortLE(0) // milliseconds
            buffer.writeShortLE(0) // unknown

            // Papel shop info?
            buffer.writeShortLE(0)
            buffer.writeShortLE(0)
            buffer.writeShortLE(0)
            buffer.writeIntLE(0) // unknown
            buffer.writeLongLE((1 shl 2).toLong()) // disabled server features, 0x4 = disables mail
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
