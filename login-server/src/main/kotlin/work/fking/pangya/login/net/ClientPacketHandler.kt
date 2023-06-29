package work.fking.pangya.login.net

import io.netty.buffer.ByteBuf
import work.fking.pangya.login.LoginServer
import work.fking.pangya.login.Player

fun interface ClientPacketHandler {
    fun handle(server: LoginServer, player: Player, packet: ByteBuf)
}
