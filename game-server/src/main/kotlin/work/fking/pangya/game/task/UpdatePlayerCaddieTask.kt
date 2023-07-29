package work.fking.pangya.game.task

import org.slf4j.LoggerFactory
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.packet.outbound.EquipmentUpdateReplies
import work.fking.pangya.game.packet.outbound.EquipmentUpdateReplies.EquipmentUpdateResult.FAILED_BECAUSE_OF_DB_ERROR
import work.fking.pangya.game.packet.outbound.EquipmentUpdateReplies.EquipmentUpdateResult.SUCCESS
import work.fking.pangya.game.packet.outbound.EquipmentUpdateReplies.EquipmentUpdateType.CADDIE
import work.fking.pangya.game.packet.outbound.EquipmentUpdateReplies.EquipmentUpdateType.CHARACTER_PARTS
import work.fking.pangya.game.player.Caddie
import work.fking.pangya.game.player.Player
import java.sql.SQLException

private val LOGGER = LoggerFactory.getLogger(UpdatePlayerCaddieTask::class.java)

class UpdatePlayerCaddieTask(
    private val server: GameServer,
    private val player: Player,
    private val caddie: Caddie
) : Runnable {

    override fun run() {
        try {
            player.equipment.equipCaddie(caddie)
            server.persistenceCtx.equipmentRepository.save(player.uid, player.equipment)
            player.writeAndFlush(EquipmentUpdateReplies.ack(result = SUCCESS, type = CADDIE) {
                it.writeIntLE(caddie.uid)
            })
        } catch (e: SQLException) {
            LOGGER.error("Failed to update player (${player.username}) equipped caddie", e)
            player.writeAndFlush(EquipmentUpdateReplies.ack(result = FAILED_BECAUSE_OF_DB_ERROR, type = CADDIE))
        }
    }
}