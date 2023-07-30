package work.fking.pangya.game.task

import org.slf4j.LoggerFactory
import work.fking.pangya.game.packet.outbound.EquipmentUpdateReplies
import work.fking.pangya.game.packet.outbound.EquipmentUpdateReplies.EquipmentUpdateResult.FAILED_BECAUSE_OF_DB_ERROR
import work.fking.pangya.game.packet.outbound.EquipmentUpdateReplies.EquipmentUpdateResult.SUCCESS
import work.fking.pangya.game.packet.outbound.EquipmentUpdateReplies.EquipmentUpdateType.EQUIPPED_ITEMS
import work.fking.pangya.game.persistence.PersistenceContext
import work.fking.pangya.game.player.Player
import java.sql.SQLException

private val LOGGER = LoggerFactory.getLogger(UpdatePlayerEquippedItemsTask::class.java)

class UpdatePlayerEquippedItemsTask(
    private val persistenceCtx: PersistenceContext,
    private val player: Player,
    private val itemIffIds: IntArray
) : Runnable {

    override fun run() {
        try {
            player.equipment.updateEquippedItems(itemIffIds)
            persistenceCtx.equipmentRepository.save(persistenceCtx.noTxContext(), player.uid, player.equipment)
            player.writeAndFlush(EquipmentUpdateReplies.ack(result = SUCCESS, type = EQUIPPED_ITEMS) { buffer ->
                itemIffIds.forEach { buffer.writeIntLE(it) }
            })
        } catch (e: SQLException) {
            LOGGER.error("Failed to update player (${player.username}) equipped items", e)
            player.writeAndFlush(EquipmentUpdateReplies.ack(result = FAILED_BECAUSE_OF_DB_ERROR, type = EQUIPPED_ITEMS))
        }
    }
}