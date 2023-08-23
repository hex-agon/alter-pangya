package work.fking.pangya.game.player

import work.fking.pangya.game.model.IffContainer
import java.lang.Math.addExact
import kotlin.math.max

class Inventory(override val entries: MutableList<Item> = ArrayList()) : IffContainer<Item> {

    fun add(item: Item) {
        entries.add(item)
    }

    fun incrementByUid(uid: Int, amount: Int): Item {
        val item = findByUid(uid) ?: throw IllegalArgumentException("no such item with uid = $uid")
        decrement(item, amount)
        return item
    }

    fun incrementByIffId(iffId: Int, amount: Int): Item {
        val item = findByIffId(iffId) ?: throw IllegalArgumentException("no such item with iffId = $iffId")
        decrement(item, amount)
        return item
    }

    private fun increment(item: Item, amount: Int) {
        require(amount > 0) { "amount must be positive" }
        item.quantity = addExact(item.quantity, amount)
    }

    fun decrementByUid(uid: Int, amount: Int): Item {
        val item = findByUid(uid) ?: throw IllegalArgumentException("no such item with uid = $uid")
        decrement(item, amount)
        return item
    }

    fun decrementByIffId(iffId: Int, amount: Int): Item {
        val item = findByIffId(iffId) ?: throw IllegalArgumentException("no such item with iffId = $iffId")
        decrement(item, amount)
        return item
    }

    private fun decrement(item: Item, amount: Int) {
        require(item.quantifiable()) { "item is not quantifiable" }
        require(amount > 0) { "amount must be positive" }
        item.quantity = max(item.quantity - amount, 0)

        if (item.quantity == 0) {
            entries.remove(item)
        }
    }
}
