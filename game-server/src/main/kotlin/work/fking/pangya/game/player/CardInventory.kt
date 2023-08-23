package work.fking.pangya.game.player

import work.fking.pangya.game.model.IffContainer

class CardInventory(override val entries: MutableList<Card> = mutableListOf()) : IffContainer<Card> {

    fun add(card: Card) {
        entries.add(card)
    }
}