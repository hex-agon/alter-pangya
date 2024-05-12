package work.fking.pangya.game.task.myroom

import org.slf4j.LoggerFactory
import work.fking.pangya.game.packet.outbound.MyRoomEquipmentUpdateReplies
import work.fking.pangya.game.packet.outbound.MyRoomEquipmentUpdateReplies.MyRoomEquipmentUpdateResult.FAILED_BECAUSE_OF_DB_ERROR
import work.fking.pangya.game.packet.outbound.MyRoomEquipmentUpdateReplies.MyRoomEquipmentUpdateResult.SUCCESS
import work.fking.pangya.game.packet.outbound.MyRoomEquipmentUpdateReplies.MyRoomEquipmentUpdateType.CHARACTER_PARTS
import work.fking.pangya.game.persistence.PersistenceContext
import work.fking.pangya.game.player.Character
import work.fking.pangya.game.player.Player
import work.fking.pangya.game.player.write
import java.sql.SQLException

private val LOGGER = LoggerFactory.getLogger(MyRoomUpdateCharacterPartsTask::class.java)

class MyRoomUpdateCharacterPartsTask(
    private val persistenceCtx: PersistenceContext,
    private val player: Player,
    private val character: Character
) : Runnable {

    override fun run() {
        try {
            persistenceCtx.noTx { tx -> characterRepository.save(tx, player.uid, character) }
            player.equippedCharacter().updateParts(character)
            player.writeAndFlush(
                MyRoomEquipmentUpdateReplies.ack(result = SUCCESS, type = CHARACTER_PARTS) { buffer ->
                    buffer.write(character)
                })
        } catch (e: SQLException) {
            LOGGER.error("Failed to update player (${player.username}) character parts", e)
            player.writeAndFlush(MyRoomEquipmentUpdateReplies.ack(result = FAILED_BECAUSE_OF_DB_ERROR, type = CHARACTER_PARTS))
        }
    }
}