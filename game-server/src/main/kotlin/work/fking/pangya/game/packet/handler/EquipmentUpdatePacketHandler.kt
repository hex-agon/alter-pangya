package work.fking.pangya.game.packet.handler

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufUtil
import org.slf4j.LoggerFactory
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.net.ClientPacketHandler
import work.fking.pangya.game.packet.outbound.EquipmentUpdateReplies
import work.fking.pangya.game.player.EQUIPPED_ITEMS_SIZE
import work.fking.pangya.game.player.Player
import work.fking.pangya.game.player.readCharacter

private val LOGGER = LoggerFactory.getLogger(EquipmentUpdatePacketHandler::class.java)

class EquipmentUpdatePacketHandler : ClientPacketHandler {

    override fun handle(server: GameServer, player: Player, packet: ByteBuf) {
        when (val type = packet.readUnsignedByte().toInt()) {
            0 -> handleUpdateCharacterParts(player, packet)
            1 -> handleUpdateCaddie(player, packet)
            2 -> handleEquippedItems(player, packet)
            3 -> handleUpdateComet(player, packet)
            4 -> handleUpdateDecoration(packet)
            5 -> handleEquipCharacter(player, packet)
            8 -> handleMascot(packet)
            9 -> handleEquipPlayerPreview(packet)
            else -> println("Unhandled equipment update type $type")
        }
    }

    private fun handleUpdateCharacterParts(player: Player, buffer: ByteBuf) {
        println(buffer.readableBytes())
        println(ByteBufUtil.hexDump(buffer))
        val character = buffer.readCharacter()
        println(buffer.readableBytes())
        LOGGER.debug("Updating player {} character parts", player.nickname)
        player.equippedCharacter().updateParts(character.partIffIds, character.partUids)
        player.writeAndFlush(EquipmentUpdateReplies.equipCharacterPartsAck(character))
    }

    private fun handleUpdateCaddie(player: Player, buffer: ByteBuf) {
        val caddieUid = buffer.readIntLE()
        LOGGER.debug("Updating player {} caddie to {}", player.nickname, Integer.toHexString(caddieUid))
        val caddie = player.caddieRoster.findByUid(caddieUid) ?: throw IllegalStateException("Player ${player.nickname} tried to equip a caddie it does not own")
        player.equipment.equipCaddie(caddie)
        player.writeAndFlush(EquipmentUpdateReplies.equipCaddieAck(caddie))
    }

    private fun handleUpdateComet(player: Player, buffer: ByteBuf) {
        val cometIffId = buffer.readIntLE()
        LOGGER.debug("Updating player {} comet to {}", player.nickname, Integer.toHexString(cometIffId))
        val comet = player.inventory.findByIffId(cometIffId) ?: throw IllegalStateException("Player ${player.nickname} tried to equip a comet it does not own")
        player.equipment.equipComet(comet)
        player.writeAndFlush(EquipmentUpdateReplies.equipCometAck(comet))
    }

    private fun handleUpdateDecoration(buffer: ByteBuf) {
    }

    private fun handleEquipCharacter(player: Player, buffer: ByteBuf) {
        val characterUid = buffer.readIntLE()
        LOGGER.debug("Updating player {} equipped character to {}", player.nickname, Integer.toHexString(characterUid))
        val character = player.characterRoster.findByUid(characterUid) ?: throw IllegalStateException("Player ${player.nickname} tried to equip a character it does not own")
        player.equipment.equipCharacter(character)
        player.writeAndFlush(EquipmentUpdateReplies.equipCharacterAck(character))
    }

    private fun handleEquippedItems(player: Player, buffer: ByteBuf) {
        val equippedItems = IntArray(EQUIPPED_ITEMS_SIZE) { buffer.readIntLE() }
        player.equipment.updateEquippedItems(equippedItems)
        player.writeAndFlush(EquipmentUpdateReplies.equipItemsAck(equippedItems))
    }

    private fun handleMascot(buffer: ByteBuf) {
    }

    private fun handleEquipPlayerPreview(buffer: ByteBuf) {
    }
}
