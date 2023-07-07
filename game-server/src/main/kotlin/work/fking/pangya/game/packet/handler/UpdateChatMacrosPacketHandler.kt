package work.fking.pangya.game.packet.handler

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.net.ClientPacketHandler
import work.fking.pangya.game.player.Player
import work.fking.pangya.networking.protocol.readFixedSizeString

class UpdateChatMacrosPacketHandler : ClientPacketHandler {
    companion object {
        private const val MAX_MACROS = 9
        private const val MACRO_LENGTH = 64
    }

    override fun handle(server: GameServer, player: Player, packet: ByteBuf) {
        val macros = arrayOfNulls<String>(MAX_MACROS)
        for (i in 0 until MAX_MACROS) {
            macros[i] = packet.readFixedSizeString(MACRO_LENGTH)
        }
    }
}
