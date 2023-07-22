package work.fking.pangya.game.persistence

import work.fking.pangya.game.player.Inventory
import work.fking.pangya.game.player.Item
import work.fking.pangya.game.player.Player
import java.util.concurrent.atomic.AtomicInteger

interface InventoryRepository {
    fun load(playerId: Int): Inventory
    fun saveItem(player: Player, item: Item): Item
}

class DefaultInventoryRepository : InventoryRepository {
    private val uidSequence = AtomicInteger(1)

    override fun load(playerId: Int): Inventory = Inventory(mutableListOf(
        Item(uid = 200, iffId = 0x10000000),
        Item(uid = 300, iffId = 0x14000000),
    ))
    override fun saveItem(player: Player, item: Item): Item = item.copy(uid = uidSequence.getAndIncrement())
}