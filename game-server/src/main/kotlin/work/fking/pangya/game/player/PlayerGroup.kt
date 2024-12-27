package work.fking.pangya.game.player

import java.util.concurrent.locks.ReentrantReadWriteLock
import java.util.function.Consumer
import kotlin.concurrent.read
import kotlin.concurrent.write

/**
 * A thread-safe group of players that contains bulk-operations.
 */
class PlayerGroup(
    val capacity: Int = -1
) {
    private val lock = ReentrantReadWriteLock()
    private val players: MutableMap<Int, Player> = LinkedHashMap()

    fun find(connectionId: Int): Player? {
        lock.read {
            return players[connectionId]
        }
    }

    fun find(predicate: (Player) -> Boolean): Player? {
        lock.read {
            for ((_, player) in players) {
                if (predicate.invoke(player)) return player
            }
        }
        return null
    }

    fun add(player: Player): Boolean {
        lock.write {
            if (capacity != -1 && players.size >= capacity) return false
            players[player.connectionId] = player
            return true
        }
    }

    fun remove(player: Player) {
        lock.write {
            players.remove(player.connectionId)
        }
    }

    fun count(): Int {
        lock.read {
            return players.size
        }
    }

    fun forEach(action: Consumer<Player>) {
        lock.read {
            players.values.forEach(action)
        }
    }

    fun forEach(action: (Player) -> Unit) {
        lock.read {
            players.values.forEach(action)
        }
    }

    fun broadcast(message: Any) {
        forEach { it.writeAndFlush(message) }
    }
}