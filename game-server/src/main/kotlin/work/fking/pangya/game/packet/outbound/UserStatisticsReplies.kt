package work.fking.pangya.game.packet.outbound

import work.fking.pangya.game.player.Player
import work.fking.pangya.game.player.PlayerBasicInfo
import work.fking.pangya.game.player.PlayerStatistics
import work.fking.pangya.game.player.PlayerTrophies
import work.fking.pangya.game.player.write
import work.fking.pangya.game.room.Course
import work.fking.pangya.game.room.CourseStatistics
import work.fking.pangya.networking.protocol.OutboundPacket

object UserStatisticsReplies {

    fun username(requestType: Int, player: Player): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x157)
            buffer.writeByte(requestType)
            buffer.writeIntLE(player.uid)
            PlayerBasicInfo().encode(buffer, player)
            buffer.writeZero(4) // unknown extra bytes
        }
    }

    fun character(player: Player): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x15e)
            buffer.writeIntLE(player.uid)
            player.equippedCharacter().encode(buffer)
        }
    }

    fun equipment(requestType: Int, player: Player): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x156)
            buffer.writeByte(requestType)
            buffer.writeIntLE(player.uid)
            player.equipment.encode(buffer)
        }
    }

    fun userStatistic(requestType: Int, playerUid: Int, statistics: PlayerStatistics): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x158)
            buffer.writeByte(requestType)
            buffer.writeIntLE(playerUid)
            buffer.write(statistics)
        }
    }

    fun courseStatistic(requestType: Int, playerUid: Int): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x15c)
            buffer.writeByte(requestType)
            buffer.writeIntLE(playerUid)

            // standard mode count
            buffer.writeIntLE(3)
            CourseStatistics(Course.BLUE_LAGOON).serialize(buffer)
            CourseStatistics(Course.SILVIA_CANNON).serialize(buffer)
            CourseStatistics(Course.SEPIA_WIND).serialize(buffer)

            // assist mode count, seems unused
            buffer.writeIntLE(1)
            CourseStatistics(Course.BLUE_LAGOON).serialize(buffer)
        }
    }

    fun trophies(requestType: Int, playerUid: Int): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x159)
            buffer.writeByte(requestType)
            buffer.writeIntLE(playerUid)
            PlayerTrophies().encode(buffer)
        }
    }

    fun ack(valid: Boolean, requestType: Int, playerUid: Int): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x89)
            buffer.writeIntLE(if (valid) 1 else 2) // 1 = valid, 2 = error
            buffer.writeByte(requestType)
            buffer.writeIntLE(playerUid)
        }
    }
}
