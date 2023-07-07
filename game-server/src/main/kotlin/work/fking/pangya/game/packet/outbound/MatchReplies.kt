package work.fking.pangya.game.packet.outbound

import work.fking.pangya.common.Rand
import work.fking.pangya.game.player.Player
import work.fking.pangya.game.room.Room
import work.fking.pangya.game.room.RoomPlayer
import work.fking.pangya.game.room.match.Hole
import work.fking.pangya.game.room.match.MatchState
import work.fking.pangya.game.room.match.ShotCommitData
import work.fking.pangya.game.room.match.TourneyShotData
import work.fking.pangya.game.room.match.write
import work.fking.pangya.game.room.write
import work.fking.pangya.networking.protocol.OutboundPacket
import work.fking.pangya.networking.protocol.writeLocalDateTime

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

    fun start76(room: Room, matchState: MatchState): OutboundPacket {
        return OutboundPacket { buffer ->
            with(buffer) {
                writeShortLE(0x76)
                writeByte(room.settings.type.uiType)
                writeIntLE(1)
                writeLocalDateTime(matchState.startTime)
            }
        }
    }

    fun matchInfo(room: Room, matchState: MatchState): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x52)
            buffer.write(matchState.course)
            buffer.writeByte(room.settings.type.uiType)
            buffer.write(matchState.holeMode)
            buffer.writeByte(matchState.holeCount)
            buffer.writeIntLE(room.settings.trophyIffId())
            buffer.writeIntLE(matchState.shotTimeMs)
            buffer.writeIntLE(matchState.gameTimeMs)
            matchState.holes.forEach { buffer.write(it) }
            buffer.writeIntLE(Rand.nextInt())
            buffer.writeZero(18)
        }
    }

    fun holeWeather(hole: Hole): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x9e)
            buffer.write(hole.weather)
            buffer.writeByte(0)
        }
    }

    fun holeWind(hole: Hole): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x5b)
            buffer.writeByte(hole.wind)
            buffer.writeByte(0) // silent wind?
            buffer.writeShortLE(hole.windDirection)
            buffer.writeByte(1) // sets, 0 adds value
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

    fun versusPlayerAimRotate(player: Player, rotation: Float): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x56)
            buffer.writeIntLE(player.connectionId)
            buffer.writeFloatLE(rotation)
        }
    }

    fun versusPlayerShotCommit(player: RoomPlayer, shotData: ShotCommitData): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x55)
            buffer.writeIntLE(player.connectionId)
            shotData.encode(buffer)
        }
    }

    fun versusPlayerTurnEnd(player: RoomPlayer): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0xcc)
            buffer.writeIntLE(player.connectionId)
            buffer.writeIntLE(0) // sizeof something
        }
    }

    fun versusPlayerTurnStart(player: RoomPlayer): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x63)
            buffer.writeIntLE(player.connectionId)
        }
    }

    fun versusFinishHole(): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x65)
        }
    }

    // used for drawing the white dots across the map
    fun gameTourneyShotGhost(player: RoomPlayer, x: Float, z: Float, shotFlags: Int, frames: Int): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x6e)
            buffer.writeIntLE(player.connectionId)

            buffer.writeByte(player.currentHole)

            buffer.writeFloatLE(x)
            buffer.writeFloatLE(z)

            buffer.writeIntLE(shotFlags)
            buffer.writeShortLE(frames)
        }
    }

    fun gameTourneyShotAck(player: RoomPlayer, tourneyShotData: TourneyShotData): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x1f7)
            buffer.writeIntLE(player.connectionId)
            buffer.writeIntLE(1) // hole
            tourneyShotData.serialize(buffer)
        }
    }
}