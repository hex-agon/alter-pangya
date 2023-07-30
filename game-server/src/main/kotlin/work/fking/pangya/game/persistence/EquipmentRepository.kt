package work.fking.pangya.game.persistence

import org.jooq.RecordMapper
import work.fking.pangya.game.persistence.jooq.keys.PLAYER_EQUIPMENT_PKEY
import work.fking.pangya.game.persistence.jooq.tables.records.PlayerEquipmentRecord
import work.fking.pangya.game.persistence.jooq.tables.references.PLAYER_EQUIPMENT
import work.fking.pangya.game.player.Equipment

interface EquipmentRepository {
    fun load(txCtx: TransactionalContext, playerUid: Int): Equipment
    fun save(txCtx: TransactionalContext, playerUid: Int, equipment: Equipment)
}

class InMemoryEquipmentRepository : EquipmentRepository {
    private val equipments = mutableMapOf<Int, Equipment>()

    override fun load(txCtx: TransactionalContext, playerUid: Int): Equipment = Equipment(
        clubSetUid = 200,
        characterUid = 100,
        cometIffId = 0x14000000
    )

    override fun save(txCtx: TransactionalContext, playerUid: Int, equipment: Equipment) {
    }
}

class JooqEquipmentRepository : EquipmentRepository {
    private val equipmentMapper = RecordMapper<PlayerEquipmentRecord, Equipment> {
        Equipment(
            itemIffIds = it.itemIffIds,
            clubSetUid = it.clubSetUid,
            cometIffId = it.cometIffId,
            characterUid = it.characterUid,
            caddieUid = it.caddieUid
        )
    }

    override fun load(txCtx: TransactionalContext, playerUid: Int): Equipment {
        return txCtx.jooq().selectFrom(PLAYER_EQUIPMENT)
            .where(PLAYER_EQUIPMENT.ACCOUNT_UID.eq(playerUid))
            .fetchOne(equipmentMapper) ?: throw IllegalStateException("could not load equipment for playerId=$playerUid")
    }

    override fun save(txCtx: TransactionalContext, playerUid: Int, equipment: Equipment) {
        txCtx.jooq().insertInto(PLAYER_EQUIPMENT)
            .set(PLAYER_EQUIPMENT.ACCOUNT_UID, playerUid)
            .set(PLAYER_EQUIPMENT.ITEM_IFF_IDS, equipment.itemIffIds)
            .set(PLAYER_EQUIPMENT.CHARACTER_UID, equipment.characterUid)
            .set(PLAYER_EQUIPMENT.CADDIE_UID, equipment.caddieUid)
            .set(PLAYER_EQUIPMENT.CLUB_SET_UID, equipment.clubSetUid)
            .set(PLAYER_EQUIPMENT.COMET_IFF_ID, equipment.cometIffId)
            .onConflict(PLAYER_EQUIPMENT_PKEY.fields)
            .doUpdate()
            .set(PLAYER_EQUIPMENT.ITEM_IFF_IDS, equipment.itemIffIds)
            .set(PLAYER_EQUIPMENT.CHARACTER_UID, equipment.characterUid)
            .set(PLAYER_EQUIPMENT.CADDIE_UID, equipment.caddieUid)
            .set(PLAYER_EQUIPMENT.CLUB_SET_UID, equipment.clubSetUid)
            .set(PLAYER_EQUIPMENT.COMET_IFF_ID, equipment.cometIffId)
            .execute()
    }
}