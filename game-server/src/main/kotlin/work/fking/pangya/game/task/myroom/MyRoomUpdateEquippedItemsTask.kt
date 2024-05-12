package work.fking.pangya.game.task.myroom

import org.slf4j.LoggerFactory
import work.fking.pangya.game.packet.outbound.MyRoomEquipmentUpdateReplies
import work.fking.pangya.game.packet.outbound.MyRoomEquipmentUpdateReplies.MyRoomEquipmentUpdateResult.FAILED_BECAUSE_OF_DB_ERROR
import work.fking.pangya.game.packet.outbound.MyRoomEquipmentUpdateReplies.MyRoomEquipmentUpdateResult.SUCCESS
import work.fking.pangya.game.packet.outbound.MyRoomEquipmentUpdateReplies.MyRoomEquipmentUpdateType.EQUIPPED_ITEMS
import work.fking.pangya.game.persistence.PersistenceContext
import work.fking.pangya.game.player.Player
import java.sql.SQLException

private val LOGGER = LoggerFactory.getLogger(MyRoomUpdateEquippedItemsTask::class.java)

class MyRoomUpdateEquippedItemsTask(
    private val persistenceCtx: PersistenceContext,
    private val player: Player,
    private val itemIffIds: IntArray
) : Runnable {

    override fun run() {
        try {
            player.equipment.updateEquippedItems(itemIffIds)
            persistenceCtx.noTx { tx -> equipmentRepository.save(tx, player.uid, player.equipment) }
            player.writeAndFlush(
                MyRoomEquipmentUpdateReplies.ack(result = SUCCESS, type = EQUIPPED_ITEMS) { buffer ->
                    itemIffIds.forEach { buffer.writeIntLE(it) }
                })
        } catch (e: SQLException) {
            LOGGER.error("Failed to update player (${player.username}) equipped items", e)
            player.writeAndFlush(MyRoomEquipmentUpdateReplies.ack(result = FAILED_BECAUSE_OF_DB_ERROR, type = EQUIPPED_ITEMS))
        }
    }
}