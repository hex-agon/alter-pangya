package work.fking.pangya.game.packet.handler

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.net.ClientPacketHandler
import work.fking.pangya.game.packet.outbound.EquipmentUpdateReplies.EquipmentUpdateType
import work.fking.pangya.game.packet.outbound.EquipmentUpdateReplies.EquipmentUpdateType.CADDIE
import work.fking.pangya.game.packet.outbound.EquipmentUpdateReplies.EquipmentUpdateType.CHARACTER
import work.fking.pangya.game.packet.outbound.EquipmentUpdateReplies.EquipmentUpdateType.CHARACTER_PARTS
import work.fking.pangya.game.packet.outbound.EquipmentUpdateReplies.EquipmentUpdateType.COMET_CLUBSET
import work.fking.pangya.game.packet.outbound.EquipmentUpdateReplies.EquipmentUpdateType.CUT_IN
import work.fking.pangya.game.packet.outbound.EquipmentUpdateReplies.EquipmentUpdateType.DECORATION
import work.fking.pangya.game.packet.outbound.EquipmentUpdateReplies.EquipmentUpdateType.EQUIPPED_ITEMS
import work.fking.pangya.game.packet.outbound.EquipmentUpdateReplies.EquipmentUpdateType.MASCOT
import work.fking.pangya.game.player.EQUIPPED_ITEMS_SIZE
import work.fking.pangya.game.player.Player
import work.fking.pangya.game.player.nullCaddie
import work.fking.pangya.game.player.readCharacter
import work.fking.pangya.game.task.UpdatePlayerCaddieTask
import work.fking.pangya.game.task.UpdatePlayerCharacterPartsTask
import work.fking.pangya.game.task.UpdatePlayerCharacterTask
import work.fking.pangya.game.task.UpdatePlayerCometClubSetTask
import work.fking.pangya.game.task.UpdatePlayerEquippedItemsTask

class EquipmentUpdatePacketHandler : ClientPacketHandler {

    override fun handle(server: GameServer, player: Player, packet: ByteBuf) {
        val typeId = packet.readUnsignedByte().toInt()
        val type = EquipmentUpdateType.entries.find { it.id == typeId } ?: throw IllegalStateException("Unsupported equipment update type=$typeId")

        when (type) {
            CHARACTER_PARTS -> handleUpdateCharacterParts(server, player, packet)
            CADDIE -> handleUpdateCaddie(server, player, packet)
            EQUIPPED_ITEMS -> handleEquippedItems(server, player, packet)
            COMET_CLUBSET -> handleUpdateComet(server, player, packet)
            DECORATION -> handleUpdateDecoration(packet)
            CHARACTER -> handleEquipCharacter(server, player, packet)
            MASCOT -> handleMascot(packet)
            CUT_IN -> handleEquipCutIn(packet)
        }
    }

    private fun handleUpdateCharacterParts(server: GameServer, player: Player, buffer: ByteBuf) {
        val character = buffer.readCharacter()
        server.runTask(
            UpdatePlayerCharacterPartsTask(
                persistenceCtx = server.persistenceCtx,
                player = player,
                character = character
            )
        )
    }

    private fun handleUpdateCaddie(server: GameServer, player: Player, buffer: ByteBuf) {
        val caddieUid = buffer.readIntLE()
        val caddie = if (caddieUid == 0) {
            nullCaddie()
        } else {
            player.caddieRoster.findByUid(caddieUid) ?: throw IllegalStateException("Player ${player.nickname} tried to equip a caddie it does not own ($caddieUid)")
        }
        server.runTask(
            UpdatePlayerCaddieTask(
                persistenceCtx = server.persistenceCtx,
                player = player,
                caddie = caddie
            )
        )
    }

    private fun handleUpdateComet(server: GameServer, player: Player, buffer: ByteBuf) {
        val cometIffId = buffer.readIntLE()
        val clubSetUid = buffer.readIntLE()
        val comet = player.inventory.findByIffId(cometIffId) ?: throw IllegalStateException("Player ${player.nickname} tried to equip a comet it does not own ($cometIffId)")
        val clubSet = player.inventory.findByUid(clubSetUid) ?: throw IllegalStateException("Player ${player.nickname} tried to equip a clubSet it does not own ($clubSetUid)")
        server.runTask(
            UpdatePlayerCometClubSetTask(
                persistenceCtx = server.persistenceCtx,
                player = player,
                comet = comet,
                clubSet = clubSet
            )
        )
    }

    private fun handleUpdateDecoration(buffer: ByteBuf) {
    }

    private fun handleEquipCharacter(server: GameServer, player: Player, buffer: ByteBuf) {
        val characterUid = buffer.readIntLE()
        val character = player.characterRoster.findByUid(characterUid) ?: throw IllegalStateException("Player ${player.nickname} tried to equip a character it does not own")
        server.runTask(
            UpdatePlayerCharacterTask(
                persistenceCtx = server.persistenceCtx,
                player = player,
                character = character
            )
        )
    }

    private fun handleEquippedItems(server: GameServer, player: Player, buffer: ByteBuf) {
        val itemIffIds = IntArray(EQUIPPED_ITEMS_SIZE) { buffer.readIntLE() }
        server.runTask(
            UpdatePlayerEquippedItemsTask(
                persistenceCtx = server.persistenceCtx,
                player = player,
                itemIffIds = itemIffIds
            )
        )
    }

    private fun handleMascot(buffer: ByteBuf) {
    }

    private fun handleEquipCutIn(buffer: ByteBuf) {
    }
}
