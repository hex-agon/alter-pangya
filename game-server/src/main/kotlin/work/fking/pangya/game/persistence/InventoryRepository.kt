package work.fking.pangya.game.persistence

import org.jooq.DSLContext
import org.jooq.RecordMapper
import work.fking.pangya.game.persistence.jooq.keys.PLAYER_INVENTORY_ITEM_PKEY
import work.fking.pangya.game.persistence.jooq.tables.records.PlayerInventoryItemRecord
import work.fking.pangya.game.persistence.jooq.tables.references.PLAYER_INVENTORY_ITEM
import work.fking.pangya.game.player.Inventory
import work.fking.pangya.game.player.Item
import java.util.concurrent.atomic.AtomicInteger

interface InventoryRepository {
    fun load(playerUid: Int): Inventory
    fun saveItem(playerUid: Int, item: Item): Item
}

class InMemoryInventoryRepository : InventoryRepository {
    private val uidSequence = AtomicInteger(1)
    private val inventories = mutableMapOf<Int, MutableList<Item>>()

    private fun findInventory(playerUid: Int): MutableList<Item> = inventories.computeIfAbsent(playerUid) {
        mutableListOf(
            Item(uid = 200, iffId = 0x10000000),
            Item(uid = 300, iffId = 0x14000000),
        )
    }

    override fun load(playerUid: Int): Inventory {
        return Inventory(findInventory(playerUid))
    }

    override fun saveItem(playerUid: Int, item: Item): Item {
        val persistedItem = item.copy(uid = uidSequence.getAndIncrement())
        findInventory(playerUid).add(persistedItem)
        return persistedItem
    }
}

class JooqInventoryRepository(private val jooq: DSLContext) : InventoryRepository {
    private val itemMapper = RecordMapper<PlayerInventoryItemRecord, Item> {
        Item(
            uid = it.uid!!,
            iffId = it.iffId,
            quantity = it.quantity ?: 0,
            stats = it.stats ?: IntArray(0),
        )
    }

    override fun load(playerUid: Int): Inventory {
        val items = jooq.selectFrom(PLAYER_INVENTORY_ITEM)
            .where(PLAYER_INVENTORY_ITEM.ACCOUNT_UID.eq(playerUid))
            .fetch(itemMapper)
        return Inventory(items)
    }

    override fun saveItem(playerUid: Int, item: Item): Item {
        val insert = jooq.insertInto(PLAYER_INVENTORY_ITEM)

        if (item.uid != -1) insert.set(PLAYER_INVENTORY_ITEM.UID, item.uid)

        val uid = insert
            .set(PLAYER_INVENTORY_ITEM.ACCOUNT_UID, playerUid)
            .set(PLAYER_INVENTORY_ITEM.IFF_ID, item.iffId)
            .set(PLAYER_INVENTORY_ITEM.QUANTITY, item.quantity)
            .set(PLAYER_INVENTORY_ITEM.STATS, item.stats)
            .onConflict(PLAYER_INVENTORY_ITEM_PKEY.fields)
            .doUpdate()
            .set(PLAYER_INVENTORY_ITEM.QUANTITY, item.quantity)
            .set(PLAYER_INVENTORY_ITEM.STATS, item.stats)
            .returningResult(PLAYER_INVENTORY_ITEM.UID)
            .fetchOneInto(Int::class.java)

        return item.copy(
            uid = uid!!
        )
    }
}