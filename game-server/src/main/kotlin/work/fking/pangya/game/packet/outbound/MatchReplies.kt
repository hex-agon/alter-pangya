package work.fking.pangya.game.packet.outbound

import work.fking.pangya.common.Rand
import work.fking.pangya.game.room.match.ShotData
import work.fking.pangya.game.player.Player
import work.fking.pangya.game.room.Room
import work.fking.pangya.game.room.RoomPlayer
import work.fking.pangya.networking.protocol.OutboundPacket
import work.fking.pangya.networking.protocol.writeLocalDateTime
import java.time.LocalDateTime

object MatchReplies {
    fun start230(): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x230)
        }
    }

    fun start231(): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x231)
        }
    }

    fun start77(): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x77)
            buffer.writeIntLE(0x64)
        }
    }

    fun start76(room: Room): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x76)
            buffer.writeByte(room.settings.type.uiType)
            buffer.writeIntLE(1)
            buffer.writeLocalDateTime(LocalDateTime.now())
        }
    }

    fun start52(room: Room): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x52)
            buffer.writeByte(room.settings.course.ordinal)
            buffer.writeByte(0)
            buffer.writeByte(room.settings.holeMode.ordinal)
            buffer.writeByte(room.settings.holeCount)
            buffer.writeIntLE(0)
            buffer.writeIntLE(room.settings.shotTimeMs)
            buffer.writeIntLE(room.settings.gameTimeMs)

            // hole info, must be equal to hole count
            for (hole in 1..18) {
                buffer.writeIntLE(Rand.nextInt())
                buffer.writeByte(0)
                buffer.writeByte(room.settings.course.ordinal)
                buffer.writeByte(hole)
            }
            buffer.writeIntLE(Rand.nextInt())
            buffer.writeZero(18)
        }
    }

    fun gameHoleWeather(): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x9e)
            buffer.writeShortLE(0) // 1 = cloudy, 2 = raining
            buffer.writeByte(0)
        }
    }

    fun gameHoleWind(): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x5b)
            buffer.writeByte(0) // wind strength
            buffer.writeByte(0) // silent wind?
            buffer.writeShortLE(0) // wind direction
            buffer.writeByte(1)
        }
    }

    /**
     * Tells the client to play the player intro & start music
     */
    fun gamePlayerStartHole(player: RoomPlayer): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x53)
            buffer.writeIntLE(player.connectionId)
        }
    }

    fun gameFinishPlayerPreviewAck(): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x90)
        }
    }

    fun gamePlayerAimRotate(player: Player, rotation: Float): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x56)
            buffer.writeIntLE(player.connectionId)
            buffer.writeFloatLE(rotation)
        }
    }

    fun gamePlayerShotCommit(player: RoomPlayer, shotData: ShotData): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x55)
            buffer.writeIntLE(player.connectionId)
            shotData.encode(buffer)
        }
    }

    fun gamePlayerTurnEnd(player: RoomPlayer): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0xCC)
            buffer.writeIntLE(player.connectionId)
            buffer.writeIntLE(0)
        }
    }

    fun gamePlayerTurnStart(player: RoomPlayer): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x63)
            buffer.writeIntLE(player.connectionId)
        }
    }

    fun gameFinishHole(): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x65)
        }
    }
}