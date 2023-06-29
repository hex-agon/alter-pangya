package work.fking.pangya.game.packet.handler

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.net.ClientPacketHandler
import work.fking.pangya.game.player.Character.Companion.decode
import work.fking.pangya.game.player.Player

class EquipmentUpdatePacketHandler : ClientPacketHandler {

    companion object {
        private const val TYPE_CHARACTER = 0
        private const val TYPE_EQUIPPED_ITEMS = 2
        private const val TYPE_UNKNOWN9 = 9
    }

    override fun handle(server: GameServer, player: Player, packet: ByteBuf) {
        when (val type = packet.readUnsignedByte().toInt()) {
            TYPE_CHARACTER -> decodeCharacter(packet)
            TYPE_EQUIPPED_ITEMS -> decodeEquippedItems(packet)
            TYPE_UNKNOWN9 -> decodeUnknown9(packet)
            else -> println("Unhandled type $type")
        }
    }

    private fun decodeCharacter(buffer: ByteBuf) {
        val character = decode(buffer)
    }

    private fun decodeEquippedItems(buffer: ByteBuf) {
        for (i in 0..9) {
            buffer.readIntLE()
        }
    }

    private fun decodeUnknown9(buffer: ByteBuf) {
        buffer.skipBytes(20)
    }

}
