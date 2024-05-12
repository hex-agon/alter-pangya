package work.fking.pangya.game.task.myroom

import org.slf4j.LoggerFactory
import work.fking.pangya.game.packet.outbound.MyRoomEquipmentUpdateReplies
import work.fking.pangya.game.packet.outbound.MyRoomEquipmentUpdateReplies.MyRoomEquipmentUpdateResult.FAILED_BECAUSE_OF_DB_ERROR
import work.fking.pangya.game.packet.outbound.MyRoomEquipmentUpdateReplies.MyRoomEquipmentUpdateResult.SUCCESS
import work.fking.pangya.game.packet.outbound.MyRoomEquipmentUpdateReplies.MyRoomEquipmentUpdateType.COMET_CLUBSET
import work.fking.pangya.game.persistence.PersistenceContext
import work.fking.pangya.game.player.Item
import work.fking.pangya.game.player.Player
import java.sql.SQLException

private val LOGGER = LoggerFactory.getLogger(MyRoomUpdateCometClubSetTask::class.java)

class MyRoomUpdateCometClubSetTask(
    private val persistenceCtx: PersistenceContext,
    private val player: Player,
    private val comet: Item,
    private val clubSet: Item
) : Runnable {

    override fun run() {
        try {
            player.equipment.equipComet(comet)
            player.equipment.equipClubSet(clubSet)
            persistenceCtx.noTx { tx -> equipmentRepository.save(tx, player.uid, player.equipment) }
            player.writeAndFlush(MyRoomEquipmentUpdateReplies.ack(result = SUCCESS, type = COMET_CLUBSET) { buffer ->
                buffer.writeIntLE(comet.iffId)
                buffer.writeIntLE(clubSet.uid)
            })
        } catch (e: SQLException) {
            LOGGER.error("Failed to update player (${player.username}) equipped comet", e)
            player.writeAndFlush(MyRoomEquipmentUpdateReplies.ack(result = FAILED_BECAUSE_OF_DB_ERROR, type = COMET_CLUBSET))
        }
    }
}