package work.fking.pangya.game.room

import io.netty.buffer.ByteBuf
import org.slf4j.LoggerFactory
import work.fking.pangya.game.packet.outbound.RoomReplies
import work.fking.pangya.game.player.Player
import work.fking.pangya.networking.protocol.writeFixedSizeString
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class Room(
    val id: Int,
    private val settings: RoomSettings
) {
    companion object {
        @JvmStatic
        private val LOGGER = LoggerFactory.getLogger(Room::class.java)
    }

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
            player.write(RoomReplies.joinAck(this))
            player.writeAndFlush(RoomReplies.roomCensusList(players))
        }
    }

    fun removePlayer(player: Player) {
        playersLock.withLock {
            player.currentRoom = null
            players.remove(findSelf(player))

            if (ownerUid == player.uid) {
                ownerUid = findNewOwner()
                LOGGER.debug("${player.nickname} was the owner of room $id, new owner is now $ownerUid")
            }
        }
    }

    fun playerCount(): Int {
        playersLock.withLock {
            return players.size
        }
    }

    private fun findSelf(player: Player): RoomPlayer {
        playersLock.withLock {
            return players.firstOrNull { it.player == player } ?: throw IllegalStateException("Player ${player.nickname} was not found in room $id")
        }
    }

    private fun findNewOwner(): Int {
        playersLock.withLock {
            return players.firstNotNullOfOrNull { it.player.uid } ?: -1
        }
    }

    fun handleUpdates(updates: List<RoomUpdate>) {
        LOGGER.debug("Updating room $id with $updates")
        updates.forEach { settings.handleUpdate(it) }
        broadcast(RoomReplies.roomSettings(this))
    }

    private fun broadcast(message: Any) {
        playersLock.withLock {
            players.forEach { it.player.writeAndFlush(message) }
        }
    }

    fun encodeSettings(buffer: ByteBuf) {
        settings.encode(buffer)
    }

    fun encodeInfo(buffer: ByteBuf) {
        buffer.writeFixedSizeString(settings.name, 64)
        buffer.writeByte(if (settings.password != null) 1 else 0)
        buffer.writeByte(1)
        buffer.writeByte(0)
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
        buffer.writeIntLE(0)
        buffer.writeIntLE(0)
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

