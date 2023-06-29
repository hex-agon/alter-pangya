package work.fking.pangya.game.net

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.player.Player

fun interface ClientPacketHandler {
    fun handle(server: GameServer, player: Player, packet: ByteBuf)
}
