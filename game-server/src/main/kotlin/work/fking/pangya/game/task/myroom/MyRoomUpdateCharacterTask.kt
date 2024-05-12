package work.fking.pangya.game.task.myroom

import org.slf4j.LoggerFactory
import work.fking.pangya.game.packet.outbound.MyRoomEquipmentUpdateReplies
import work.fking.pangya.game.packet.outbound.MyRoomEquipmentUpdateReplies.MyRoomEquipmentUpdateResult.FAILED_BECAUSE_OF_DB_ERROR
import work.fking.pangya.game.packet.outbound.MyRoomEquipmentUpdateReplies.MyRoomEquipmentUpdateResult.SUCCESS
import work.fking.pangya.game.packet.outbound.MyRoomEquipmentUpdateReplies.MyRoomEquipmentUpdateType.CHARACTER
import work.fking.pangya.game.persistence.PersistenceContext
import work.fking.pangya.game.player.Character
import work.fking.pangya.game.player.Player
import java.sql.SQLException

private val LOGGER = LoggerFactory.getLogger(MyRoomUpdateCharacterTask::class.java)

class MyRoomUpdateCharacterTask(
    private val persistenceCtx: PersistenceContext,
    private val player: Player,
    private val character: Character
) : Runnable {

    override fun run() {
        try {
            player.equipment.equipCharacter(character)
            persistenceCtx.noTx { tx -> equipmentRepository.save(tx, player.uid, player.equipment) }
            player.writeAndFlush(MyRoomEquipmentUpdateReplies.ack(result = SUCCESS, type = CHARACTER) {
                it.writeIntLE(character.uid)
            })
        } catch (e: SQLException) {
            LOGGER.error("Failed to update player (${player.username}) equipped character", e)
            player.writeAndFlush(MyRoomEquipmentUpdateReplies.ack(result = FAILED_BECAUSE_OF_DB_ERROR, type = CHARACTER))
        }
    }
}