package work.fking.pangya.game.room

import io.netty.buffer.ByteBuf
import org.slf4j.LoggerFactory
import work.fking.pangya.game.packet.outbound.MatchReplies
import work.fking.pangya.game.packet.outbound.RoomReplies
import work.fking.pangya.game.player.Player
import work.fking.pangya.game.room.match.MatchEvent
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
        LOGGER.debug("Room $id handling event {}", event)
        settings.type.matchDirector.handleMatchEvent(event)
    }

    fun handleUpdates(updates: List<RoomUpdate>) {
        if (state != RoomState.LOBBY) {
            throw IllegalStateException("Cannot handle room update, room $id is $state")
        }
        LOGGER.debug("Updating room {} with {}", id, updates)
        updates.forEach { settings.handleUpdate(it) }
        broadcast(RoomReplies.roomSettings(this))
    }

    private fun broadcast(message: Any) {
        playersLock.withLock {
            players.forEach { it.player.writeAndFlush(message) }
        }
    }

    fun startGame() {
        playersLock.withLock {
            state = RoomState.IN_GAME
            players.forEach { player ->
                player.write(MatchReplies.start230())
                player.write(MatchReplies.start231())
                player.write(MatchReplies.start77())
                player.write(MatchReplies.start76(this))
                player.writeAndFlush(MatchReplies.start52(this))
            }
        }
    }

    fun encodeSettings(buffer: ByteBuf) {
        settings.encode(buffer)
    }

    fun encodeInfo(buffer: ByteBuf) {
        buffer.writeFixedSizeString(settings.name, 64)
        buffer.writeBoolean(settings.password == null) // public room
        buffer.writeBoolean(state == RoomState.LOBBY)
        buffer.writeBoolean(state == RoomState.IN_GAME_JOINABLE)
        buffer.writeByte(settings.maxPlayers)
        buffer.writeByte(playerCount())
        buffer.writeZero(17)
        buffer.writeByte(30)
        buffer.writeByte(settings.holeCount)
        buffer.writeByte(settings.type.uiType)
        buffer.writeShortLE(id)
        buffer.writeByte(settings.holeMode.ordinal)
        buffer.writeByte(settings.course.ordinal)
        buffer.writeIntLE(settings.shotTimeMs)
        buffer.writeIntLE(settings.gameTimeMs)
        buffer.writeIntLE(settings.trophyIffId())
        buffer.writeShortLE(0)
        buffer.writeZero(66) // guildInfo
        buffer.writeIntLE(100)
        buffer.writeIntLE(100)
        buffer.writeIntLE(ownerUid)
        buffer.writeByte(settings.type.id)
        buffer.writeIntLE(settings.artifactIffId)
        buffer.writeIntLE(if (settings.naturalWind) 1 else 0)
        // event info
        buffer.writeIntLE(0)
        buffer.writeIntLE(0)
        buffer.writeIntLE(0)
        buffer.writeIntLE(0)
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

fun interface PlayerLeaveRoomListener {
    fun onPlayerLeave(room: Room, player: RoomPlayer)
}