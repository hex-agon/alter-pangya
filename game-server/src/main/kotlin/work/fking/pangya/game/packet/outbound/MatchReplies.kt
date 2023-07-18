package work.fking.pangya.game.packet.outbound

import work.fking.pangya.game.Rand
import work.fking.pangya.game.model.write
import work.fking.pangya.game.player.Player
import work.fking.pangya.game.room.Room
import work.fking.pangya.game.room.RoomPlayer
import work.fking.pangya.game.room.mapHoleCollectibles
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
            buffer.writeIntLE(matchState.randSeed) // it seems like the original server can only generate short random values

            val mapHoleCollectibles = mapHoleCollectibles[matchState.course] ?: Array(18) { emptyArray() }

            for ((holeIdx, holeCollectibles) in mapHoleCollectibles.withIndex()) {
                buffer.writeByte(holeCollectibles.size)
                for (collectible in holeCollectibles) {
                    buffer.writeIntLE(collectible.type.ordinal)
                    buffer.writeIntLE(Rand.nextInt())
                    buffer.writeIntLE(0)
                    buffer.writeIntLE(matchState.course.ordinal)
                    buffer.writeByte(holeIdx + 1)
                    buffer.writeByte(holeIdx)
                    buffer.writeShortLE(0)
                    buffer.write(collectible.position)
                    buffer.writeIntLE(collectible.type.ordinal)
                }
            }
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

    fun playerUseItem(player: RoomPlayer, itemIffId: Int, randSeed: Int): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x5a)
            buffer.writeIntLE(itemIffId)
            buffer.writeIntLE(randSeed)
            buffer.writeIntLE(player.connectionId)
        }
    }

    /**
     * Tells the client to play the player intro & start music
     */
    fun playerStartHole(player: RoomPlayer): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x53)
            buffer.writeIntLE(player.connectionId)
        }
    }

    fun finishPlayerPreviewAck(): OutboundPacket {
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
    fun tourneyShotGhost(player: RoomPlayer, x: Float, z: Float, shotFlags: Int, frames: Int): OutboundPacket {
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

    fun tourneyShotAck(player: RoomPlayer, tourneyShotData: TourneyShotData): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x1f7)
            buffer.writeIntLE(player.connectionId)
            buffer.writeIntLE(1) // hole
            tourneyShotData.serialize(buffer)
        }
    }

    fun tourneyEndingScore(): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x79)
            buffer.writeIntLE(4) // experience
            buffer.writeIntLE(0x2C000000) // trophy IffId
            buffer.writeByte(0) // trophy again?
            buffer.writeByte(0) // which team won? 0 red 1 blue 2 neither

            repeat(12) { // medals
                buffer.writeIntLE(0) // uniqueId
                buffer.writeIntLE(0) // iffId
            }
        }
    }

    fun tourneyWinnings(itemIffIds: List<Int>): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0xce)
            buffer.writeByte(0)
            buffer.writeShortLE(itemIffIds.size)
            itemIffIds.forEach { buffer.writeIntLE(it) }
        }
    }

    fun tourneyTimeout(): OutboundPacket {
        return OutboundPacket { buffer -> buffer.writeShortLE(0x8c) }
    }

    fun tourneyUpdatePlayerProgress(player: RoomPlayer): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x6d)
            buffer.writeIntLE(player.connectionId)
            buffer.writeByte(player.currentHole) // hole
            buffer.writeByte(3) // total strokes?
            buffer.writeIntLE(-1) // score
            buffer.writeLongLE(player.pang.toLong()) // pang
            buffer.writeLongLE(player.bonusPang.toLong()) // bonus pang
            buffer.writeByte(1) //  finished the hole, 1 or 0 not
        }
    }

    fun useTimeBoosterAck(player: RoomPlayer, boostValue: Float): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0xC7)
            buffer.writeFloatLE(boostValue)
            buffer.writeIntLE(player.connectionId)
        }
    }
}