package work.fking.pangya.game.task

import org.slf4j.LoggerFactory
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.packet.outbound.EquipmentUpdateReplies
import work.fking.pangya.game.packet.outbound.EquipmentUpdateReplies.EquipmentUpdateResult.FAILED_BECAUSE_OF_DB_ERROR
import work.fking.pangya.game.packet.outbound.EquipmentUpdateReplies.EquipmentUpdateResult.SUCCESS
import work.fking.pangya.game.packet.outbound.EquipmentUpdateReplies.EquipmentUpdateType.COMET_CLUBSET
import work.fking.pangya.game.player.Item
import work.fking.pangya.game.player.Player
import java.sql.SQLException

private val LOGGER = LoggerFactory.getLogger(UpdatePlayerCometClubSetTask::class.java)

class UpdatePlayerCometClubSetTask(
    private val server: GameServer,
    private val player: Player,
    private val comet: Item,
    private val clubSet: Item
) : Runnable {

    override fun run() {
        try {
            player.equipment.equipComet(comet)
            player.equipment.equipClubSet(clubSet)
            server.persistenceCtx.equipmentRepository.save(player.uid, player.equipment)
            player.writeAndFlush(EquipmentUpdateReplies.ack(result = SUCCESS, type = COMET_CLUBSET) {
                it.writeIntLE(comet.iffId)
                it.writeIntLE(clubSet.uid)
            })
        } catch (e: SQLException) {
            LOGGER.error("Failed to update player (${player.username}) equipped comet", e)
            player.writeAndFlush(EquipmentUpdateReplies.ack(result = FAILED_BECAUSE_OF_DB_ERROR, type = COMET_CLUBSET))
        }
    }
}