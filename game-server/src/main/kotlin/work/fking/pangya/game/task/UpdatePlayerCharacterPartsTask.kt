package work.fking.pangya.game.task

import org.slf4j.LoggerFactory
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.packet.outbound.EquipmentUpdateReplies
import work.fking.pangya.game.packet.outbound.EquipmentUpdateReplies.EquipmentUpdateResult.FAILED_BECAUSE_OF_DB_ERROR
import work.fking.pangya.game.packet.outbound.EquipmentUpdateReplies.EquipmentUpdateResult.SUCCESS
import work.fking.pangya.game.packet.outbound.EquipmentUpdateReplies.EquipmentUpdateType.CHARACTER_PARTS
import work.fking.pangya.game.player.Character
import work.fking.pangya.game.player.Player
import work.fking.pangya.game.player.write
import java.sql.SQLException

private val LOGGER = LoggerFactory.getLogger(UpdatePlayerCharacterPartsTask::class.java)

class UpdatePlayerCharacterPartsTask(
    private val server: GameServer,
    private val player: Player,
    private val character: Character
) : Runnable {

    override fun run() {
        try {
            server.persistenceCtx.characterRepository.save(player.uid, character)
            player.equippedCharacter().updateParts(character)
            player.writeAndFlush(EquipmentUpdateReplies.ack(result = SUCCESS, type = CHARACTER_PARTS) {
                it.write(character)
            })
        } catch (e: SQLException) {
            LOGGER.error("Failed to update player (${player.username}) character parts", e)
            player.writeAndFlush(EquipmentUpdateReplies.ack(result = FAILED_BECAUSE_OF_DB_ERROR, type = CHARACTER_PARTS))
        }
    }
}
