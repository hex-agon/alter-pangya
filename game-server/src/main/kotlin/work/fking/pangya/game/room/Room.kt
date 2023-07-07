package work.fking.pangya.game.room

import io.netty.buffer.ByteBuf
import org.slf4j.LoggerFactory
import work.fking.pangya.game.packet.outbound.MatchReplies
import work.fking.pangya.game.packet.outbound.RoomReplies
import work.fking.pangya.game.player.Player
import work.fking.pangya.game.room.match.MatchDirector
import work.fking.pangya.game.room.match.MatchEvent
import work.fking.pangya.game.room.match.MatchState
import work.fking.pangya.networking.protocol.writeFixedSizeString
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class Room(
    val id: Int,
    val settings: RoomSettings,
    private val playerLeaveListener: PlayerLeaveRoomListener
) {
    companion object {
        @JvmStatic
        private val LOGGER = LoggerFactory.getLogger(Room::class.java)
    }

    private var state: RoomState = RoomState.LOBBY
    private var matchEventHandler: MatchEventHandler? = null
    private var ownerUid = -1

    private val playersLock = ReentrantLock()
    private val players = ArrayList<RoomPlayer>() // TODO: we can't use a list because lists shuffle when elements are removed

    fun addPlayer(player: Player) {
        playersLock.withLock {
            player.currentRoom = this
            players.add(RoomPlayer(player))

            if (ownerUid == -1) {
                ownerUid = findNewOwner()
                LOGGER.debug("Room $id had no owner, ${player.nickname} is now the owner ($ownerUid)")
            }
            // for a practice room...
            player.write(RoomReplies.roomSettings(this))
            player.write(RoomReplies.joinAck(this))
            player.writeAndFlush(RoomReplies.roomCensusList(players, settings.type.extendedInfo))
        }
    }

    fun removePlayer(player: Player) {
        playersLock.withLock {
            player.currentRoom = null
            val roomPlayer = findSelf(player)
            players.remove(roomPlayer)

            if (ownerUid == player.uid) {
                ownerUid = findNewOwner()
                LOGGER.debug("${player.nickname} was the owner of room $id, new owner is now $ownerUid")
            }
            playerLeaveListener.onPlayerLeave(this, roomPlayer)
        }
    }

    fun playerCount(): Int {
        playersLock.withLock {
            return players.size
        }
    }

    fun isEmpty(): Boolean {
        return playerCount() == 0
    }

    fun findSelf(player: Player): RoomPlayer {
        playersLock.withLock {
            return players.firstOrNull { it.player == player } ?: throw IllegalStateException("Player ${player.nickname} was not found in room $id")
        }
    }

    private fun findNewOwner(): Int {
        playersLock.withLock {
            return players.firstNotNullOfOrNull { it.player.uid } ?: -1
        }
    }

    fun handleMatchEvent(event: MatchEvent) {
        matchEventHandler?.onMatchEvent(event) ?: throw IllegalStateException("Room[$id] Cannot handle match event, no bound handler")
    }

    fun handleUpdates(updates: List<RoomUpdate>) {
        if (state != RoomState.LOBBY) {
            throw IllegalStateException("Cannot handle room update, room $id state is $state")
        }
        LOGGER.debug("Room [$id] update with {}", id, updates)
        updates.forEach { settings.handleUpdate(it) }
        broadcast(RoomReplies.roomSettings(this))
    }

    fun broadcast(message: Any) {
        playersLock.withLock {
            players.forEach { it.player.writeAndFlush(message) }
        }
    }

    fun startGame() {
        state = RoomState.IN_GAME
        val matchState = MatchState(
            course = settings.course,
            holeMode = settings.holeMode,
            holeCount = settings.holeCount,
            shotTimeMs = settings.shotTimeMs,
            gameTimeMs = settings.gameTimeMs
        )
        matchEventHandler = MatchEventHandler(this, matchState, settings.type.matchDirector)

        playersLock.withLock {
            players.forEach { player ->
                player.write(MatchReplies.start230())
                player.write(MatchReplies.start231())
                player.write(MatchReplies.start77())
                player.write(MatchReplies.start76(this, matchState))
                player.writeAndFlush(MatchReplies.matchInfo(this, matchState))
            }
        }
    }

    fun encodeSettings(buffer: ByteBuf) {
        settings.encode(buffer)
    }

    fun encodeInfo(buffer: ByteBuf) {
        with(buffer) {
            writeFixedSizeString(settings.name, 64)
            writeBoolean(settings.password == null) // public room
            writeBoolean(state == RoomState.LOBBY)
            writeBoolean(state == RoomState.IN_GAME_JOINABLE)
            writeByte(settings.maxPlayers)
            writeByte(playerCount())
            writeZero(17)
            writeByte(30)
            writeByte(settings.holeCount)
            writeByte(settings.type.uiType)
            writeShortLE(id)
            write(settings.holeMode)
            write(settings.course)
            writeIntLE(settings.shotTimeMs)
            writeIntLE(settings.gameTimeMs)
            writeIntLE(settings.trophyIffId())
            writeShortLE(0)
            writeZero(66) // guildInfo
            writeIntLE(100)
            writeIntLE(100)
            writeIntLE(ownerUid)
            writeByte(settings.type.id)
            writeIntLE(settings.artifactIffId)
            writeIntLE(if (settings.naturalWind) 1 else 0)
            // event info
            writeIntLE(0)
            writeIntLE(0)
            writeIntLE(0)
            writeIntLE(0)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Room

        return id == other.id
    }

    override fun hashCode(): Int {
        return id
    }
}

enum class RoomState {
    LOBBY,
    IN_GAME_JOINABLE,
    IN_GAME
}

private class MatchEventHandler(val room: Room, val matchState: MatchState, val matchDirector: MatchDirector) {
    fun onMatchEvent(event: MatchEvent) {
        matchDirector.handleMatchEvent(room, matchState, event)
    }
}

fun interface PlayerLeaveRoomListener {
    fun onPlayerLeave(room: Room, player: RoomPlayer)
}