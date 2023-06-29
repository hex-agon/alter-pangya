package work.fking.pangya.game

import work.fking.pangya.game.player.Player
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class ServerChannel(
    private val id: Int,
    private val name: String,
    private val capacity: Int,
    private val restrictions: List<Restriction>
) {
    private val playerCount = AtomicInteger()
    private val players: MutableMap<Int, Player> = ConcurrentHashMap()

    fun findPlayer(connectionId: Int): Player? {
        return players[connectionId]
    }

    fun addPlayer(player: Player) {
        players[player.connectionId()] = player
        playerCount.incrementAndGet()
    }

    fun removePlayer(player: Player) {
        players.remove(player.connectionId())
        playerCount.decrementAndGet()
    }

    fun id(): Int {
        return id
    }

    fun name(): String {
        return name
    }

    fun capacity(): Int {
        return capacity
    }

    fun restrictions(): List<Restriction> {
        return restrictions
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
