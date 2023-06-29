package work.fking.pangya.game.player

import work.fking.pangya.game.model.IffContainer

class Inventory(override val entries: MutableList<Item> = ArrayList()) : IffContainer<Item> {

    fun add(item: Item) {
        entries.add(item)
    }
}
