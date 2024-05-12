package work.fking.pangya.game.room

import io.netty.buffer.ByteBuf
import org.slf4j.LoggerFactory
import work.fking.pangya.game.packet.outbound.MatchReplies
import work.fking.pangya.game.packet.outbound.RoomReplies
import work.fking.pangya.game.player.Player
import work.fking.pangya.game.room.RoomJoinError.GAME_ALREADY_STARTED
import work.fking.pangya.game.room.RoomJoinError.ROOM_DOES_NOT_EXIST
import work.fking.pangya.game.room.RoomJoinError.ROOM_FULL
import work.fking.pangya.game.room.match.MatchDirector
import work.fking.pangya.game.room.match.MatchEvent
import work.fking.pangya.game.room.match.MatchState
import work.fking.pangya.networking.protocol.OutboundPacket
import work.fking.pangya.networking.protocol.writeFixedSizeString
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

private val LOGGER = LoggerFactory.getLogger(Room::class.java)

class Room(
    val id: Int,
    val settings: RoomSettings
) {
    var state: RoomState = RoomState.LOBBY
        private set
    var ownerUid = -1
        private set
    private var activeMatch: Match? = null

    private val playersLock = ReentrantReadWriteLock()
    private val players = ArrayList<RoomPlayer>()
    private var nextFreeSlot: Int = 1

    fun tick() {
    }

    fun attemptJoin(player: Player): RoomJoinError? {
        playersLock.write {
            player.currentRoom = this
            if (state == RoomState.PENDING_REMOVAL) {
                return ROOM_DOES_NOT_EXIST
            }
            if (state == RoomState.IN_GAME) {
                return GAME_ALREADY_STARTED
            }
            if (players.size >= settings.maxPlayers) {
                return ROOM_FULL
            }
            val roomPlayer = RoomPlayer(player, nextFreeSlot++)
            // always broadcast the player join to the room before adding it to the list
            broadcast(RoomReplies.roomCensusAdd(roomPlayer, isOwner(roomPlayer), settings.type.extendedInfo))
            players.add(roomPlayer)

            if (ownerUid == -1) {
                ownerUid = roomPlayer.uid
                LOGGER.debug("Room $id had no owner, ${player.nickname} is now the owner ($ownerUid)")
            }
            // TODO: Do we want to couple network logic with our room logic? If not, do it event based? (RoomPlayerJoin, RoomPlayerLeave etc...)
            // for a practice room...
            player.write(RoomReplies.roomSettings(this))
            player.write(RoomReplies.joinAck(this))
            player.writeAndFlush(RoomReplies.roomCensusList(players, isOwner(roomPlayer), settings.type.extendedInfo))
        }
        return null
    }

    fun removePlayer(player: Player) {
        playersLock.write {
            player.currentRoom = null
            val roomPlayer = findSelf(player)
            players.remove(roomPlayer)
            nextFreeSlot--

            broadcast(RoomReplies.roomCensusRemove(roomPlayer))

            if (ownerUid != player.uid) return

            val owner = players.firstOrNull()
            ownerUid = owner?.player?.uid ?: -1

            if (ownerUid == -1) {
                state = RoomState.PENDING_REMOVAL
            }
            LOGGER.debug("${player.nickname} was the owner of room $id, new owner is now $ownerUid")
        }
    }

    fun playerCount(): Int {
        playersLock.read {
            return players.size
        }
    }

    fun isOwner(roomPlayer: RoomPlayer): Boolean = roomPlayer.uid == ownerUid

    fun findSelf(player: Player): RoomPlayer {
        playersLock.read {
            return players.firstOrNull { it.player == player } ?: throw IllegalStateException("Player ${player.nickname} was not found in room $id")
        }
    }

    fun handleMatchEvent(event: MatchEvent) {
        activeMatch?.onMatchEvent(this, event)
    }

    fun handleUpdates(updates: List<RoomUpdate>) {
        if (state != RoomState.LOBBY) {
            throw IllegalStateException("Cannot handle room update, room $id state is $state")
        }
        LOGGER.debug("Room [$id] update with {}", updates)
        updates.forEach { settings.handleUpdate(it) }
        broadcast(RoomReplies.roomSettings(this))
    }

    fun broadcast(message: OutboundPacket) {
        playersLock.read {
            players.forEach { it.player.writeAndFlush(message) }
        }
    }

    fun startGame() {
        state = RoomState.IN_GAME
        val matchState = MatchState(
            course = settings.course,
            holeMode = settings.holeMode,
            holeCount = settings.holeCount,
            shotTime = settings.shotTime,
            gameTime = settings.gameTime
        )
        activeMatch = Match(matchState, settings.type.matchDirector)

        playersLock.write {
            players.forEach { player ->
                player.resetGameState()
                player.write(MatchReplies.start230())
                player.write(MatchReplies.start231())
                player.write(MatchReplies.start77())
                player.write(MatchReplies.start76(this, matchState))
                player.writeAndFlush(MatchReplies.matchInfo(this, matchState))
            }
        }
    }

    fun endGame() {
        state = RoomState.LOBBY
        activeMatch = null
        playersLock.write { players.forEach { player -> player.resetLobbyState() } }
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

fun ByteBuf.write(room: Room) {
    with(room) {
        writeFixedSizeString(settings.name, 64)
        writeBoolean(settings.password.isNullOrBlank()) // public room
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
        writeIntLE(settings.shotTime.toMillis().toInt())
        writeIntLE(settings.gameTime.toMillis().toInt())
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

enum class RoomState {
    LOBBY,
    IN_GAME_JOINABLE,
    IN_GAME,
    PENDING_REMOVAL
}

private class Match(val matchState: MatchState, val matchDirector: MatchDirector) {
    fun onMatchEvent(room: Room, event: MatchEvent) {
        matchDirector.handleMatchEvent(room, matchState, event)
    }
}