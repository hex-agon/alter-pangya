package work.fking.pangya.game.packet.handler

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.net.ClientPacketHandler
import work.fking.pangya.game.player.Player
import work.fking.pangya.game.player.readCharacter

class EquipmentUpdatePacketHandler : ClientPacketHandler {

    override fun handle(server: GameServer, player: Player, packet: ByteBuf) {
        when (val type = packet.readUnsignedByte().toInt()) {
            0 -> handleUpdateCharacterParts(player, packet)
            1 -> handleUpdateCaddie(packet)
            2 -> handleEquippedItems(packet)
            3 -> handleUpdateComet(packet)
            4 -> handleUpdateDecoration(packet)
            5 -> decodeUpdateCharacter(packet)
            9 -> handleUnknown(packet)
            else -> println("Unhandled equipment update type $type")
        }
    }

    private fun handleUpdateCharacterParts(player: Player, buffer: ByteBuf) {
        val character = buffer.readCharacter(buffer)
        player.equippedCharacter().updateParts(character.partIffIds, character.partUids)
    }

    private fun handleUpdateCaddie(buffer: ByteBuf) {
    }

    private fun handleUpdateComet(buffer: ByteBuf) {
    }

    private fun handleUpdateDecoration(buffer: ByteBuf) {
    }

    private fun decodeUpdateCharacter(buffer: ByteBuf) {
    }

    private fun handleEquippedItems(buffer: ByteBuf) {
        for (i in 0..9) {
            buffer.readIntLE()
        }
    }

    private fun handleUnknown(buffer: ByteBuf) {
    }
}
