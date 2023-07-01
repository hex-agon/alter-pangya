package work.fking.pangya.game

import work.fking.pangya.game.player.Player
import work.fking.pangya.game.room.RoomManager
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class ServerChannel(
    val id: Int,
    val name: String,
    val capacity: Int,
    val restrictions: List<Restriction>
) {
    val roomManager: RoomManager = RoomManager()

    private val playerCount = AtomicInteger()
    private val players: MutableMap<Int, Player> = ConcurrentHashMap()

    fun findPlayer(connectionId: Int): Player? {
        return players[connectionId]
    }

    fun addPlayer(player: Player) {
        player.currentChannel = this
        players[player.connectionId] = player
        playerCount.incrementAndGet()
    }

    fun removePlayer(player: Player) {
        players.remove(player.connectionId)
        playerCount.decrementAndGet()
    }

    fun playerCount(): Int {
        return playerCount.get()
    }

    override fun toString(): String {
        return "ServerChannel[" +
                "id=" + id + ", " +
                "name=" + name + ", " +
                "capacity=" + capacity + ", " +
                "restrictions=" + restrictions + ']'
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ServerChannel

        return id == other.id
    }

    override fun hashCode(): Int {
        return id
    }

    enum class Restriction(private val flag: Int) {
        JUNIOR_AND_LOWER(1 shl 1),
        ROOKIES_ONLY(1 shl 3),
        BEGINNERS_AND_JUNIORS_ONLY(1 shl 4),
        JUNIORS_AND_SENIORS_ONLY(1 shl 5);

        fun flag(): Int {
            return flag
        }
    }
}
