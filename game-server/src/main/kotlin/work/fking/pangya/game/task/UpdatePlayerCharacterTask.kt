package work.fking.pangya.game.task

import org.slf4j.LoggerFactory
import work.fking.pangya.game.packet.outbound.EquipmentUpdateReplies
import work.fking.pangya.game.packet.outbound.EquipmentUpdateReplies.EquipmentUpdateResult.FAILED_BECAUSE_OF_DB_ERROR
import work.fking.pangya.game.packet.outbound.EquipmentUpdateReplies.EquipmentUpdateResult.SUCCESS
import work.fking.pangya.game.packet.outbound.EquipmentUpdateReplies.EquipmentUpdateType.CHARACTER
import work.fking.pangya.game.persistence.PersistenceContext
import work.fking.pangya.game.player.Character
import work.fking.pangya.game.player.Player
import java.sql.SQLException

private val LOGGER = LoggerFactory.getLogger(UpdatePlayerCharacterTask::class.java)

class UpdatePlayerCharacterTask(
    private val persistenceCtx: PersistenceContext,
    private val player: Player,
    private val character: Character
) : Runnable {

    override fun run() {
        try {
            player.equipment.equipCharacter(character)
            persistenceCtx.equipmentRepository.save(persistenceCtx.noTxContext(), player.uid, player.equipment)
            player.writeAndFlush(EquipmentUpdateReplies.ack(result = SUCCESS, type = CHARACTER) {
                it.writeIntLE(character.uid)
            })
        } catch (e: SQLException) {
            LOGGER.error("Failed to update player (${player.username}) equipped character", e)
            player.writeAndFlush(EquipmentUpdateReplies.ack(result = FAILED_BECAUSE_OF_DB_ERROR, type = CHARACTER))
        }
    }
}