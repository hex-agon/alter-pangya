package work.fking.pangya.game.persistence

import org.jooq.RecordMapper
import work.fking.pangya.game.persistence.jooq.keys.PLAYER_INVENTORY_CARD_PKEY
import work.fking.pangya.game.persistence.jooq.tables.records.PlayerInventoryCardRecord
import work.fking.pangya.game.persistence.jooq.tables.references.PLAYER_INVENTORY_CARD
import work.fking.pangya.game.player.Card
import work.fking.pangya.game.player.CardInventory
import java.util.concurrent.atomic.AtomicInteger

interface CardRepository {
    fun load(txCtx: TransactionalContext, playerUid: Int): CardInventory
    fun saveCard(txCtx: TransactionalContext, playerUid: Int, card: Card): Card
    fun deleteCard(txCtx: TransactionalContext, playerUid: Int, card: Card)
}

class InMemoryCardRepository : CardRepository {
    private val uidSequence = AtomicInteger(1)
    private val cardInventories = mutableMapOf<Int, MutableList<Card>>()

    private fun findCardInventory(playerUid: Int): MutableList<Card> = cardInventories.computeIfAbsent(playerUid) {
        mutableListOf()
    }

    override fun load(txCtx: TransactionalContext, playerUid: Int): CardInventory {
        return CardInventory(findCardInventory(playerUid))
    }

    override fun saveCard(txCtx: TransactionalContext, playerUid: Int, card: Card): Card {
        val persistedCard = card.copy(uid = uidSequence.get())
        findCardInventory(playerUid).add(persistedCard)
        return persistedCard
    }

    override fun deleteCard(txCtx: TransactionalContext, playerUid: Int, card: Card) {
        findCardInventory(playerUid).remove(card)
    }
}

class JooqCardInventory : CardRepository {
    private val cardMapper = RecordMapper<PlayerInventoryCardRecord, Card> {
        Card(
            uid = it.uid!!,
            iffId = it.iffId,
            quantity = it.quantity ?: 1
        )
    }

    override fun load(txCtx: TransactionalContext, playerUid: Int): CardInventory {
        val cards = txCtx.jooq().selectFrom(PLAYER_INVENTORY_CARD)
            .where(PLAYER_INVENTORY_CARD.ACCOUNT_UID.eq(playerUid))
            .fetch(cardMapper)
        return CardInventory(cards)
    }

    override fun saveCard(txCtx: TransactionalContext, playerUid: Int, card: Card): Card {
        val insert = txCtx.jooq().insertInto(PLAYER_INVENTORY_CARD)

        if (card.uid != -1) insert.set(PLAYER_INVENTORY_CARD.UID, card.uid)

        val uid = insert
            .set(PLAYER_INVENTORY_CARD.ACCOUNT_UID, playerUid)
            .set(PLAYER_INVENTORY_CARD.IFF_ID, card.iffId)
            .set(PLAYER_INVENTORY_CARD.QUANTITY, card.quantity)
            .onConflict(PLAYER_INVENTORY_CARD_PKEY.fields)
            .doUpdate()
            .set(PLAYER_INVENTORY_CARD.QUANTITY, card.quantity)
            .returningResult(PLAYER_INVENTORY_CARD.UID)
            .fetchOneInto(Int::class.java)

        return card.copy(
            uid = uid!!
        )
    }

    override fun deleteCard(txCtx: TransactionalContext, playerUid: Int, card: Card) {
        txCtx.jooq().deleteFrom(PLAYER_INVENTORY_CARD)
            .where(PLAYER_INVENTORY_CARD.UID.eq(card.uid))
    }
}