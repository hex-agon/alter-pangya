package work.fking.pangya.game.task.myroom

import org.slf4j.LoggerFactory
import work.fking.pangya.game.packet.outbound.MyRoomEquipmentUpdateReplies
import work.fking.pangya.game.packet.outbound.MyRoomEquipmentUpdateReplies.MyRoomEquipmentUpdateResult.FAILED_BECAUSE_OF_DB_ERROR
import work.fking.pangya.game.packet.outbound.MyRoomEquipmentUpdateReplies.MyRoomEquipmentUpdateResult.SUCCESS
import work.fking.pangya.game.packet.outbound.MyRoomEquipmentUpdateReplies.MyRoomEquipmentUpdateType.CADDIE
import work.fking.pangya.game.persistence.PersistenceContext
import work.fking.pangya.game.player.Caddie
import work.fking.pangya.game.player.Player
import java.sql.SQLException

private val LOGGER = LoggerFactory.getLogger(MyRoomUpdateCaddieTask::class.java)

class MyRoomUpdateCaddieTask(
    private val persistenceCtx: PersistenceContext,
    private val player: Player,
    private val caddie: Caddie
) : Runnable {

    override fun run() {
        try {
            player.equipment.equipCaddie(caddie)
            persistenceCtx.noTx { tx -> equipmentRepository.save(tx, player.uid, player.equipment) }
            player.writeAndFlush(MyRoomEquipmentUpdateReplies.ack(result = SUCCESS, type = CADDIE) {
                it.writeIntLE(caddie.uid)
            })
        } catch (e: SQLException) {
            LOGGER.error("Failed to update player (${player.username}) equipped caddie", e)
            player.writeAndFlush(MyRoomEquipmentUpdateReplies.ack(result = FAILED_BECAUSE_OF_DB_ERROR, type = CADDIE))
        }
    }
}