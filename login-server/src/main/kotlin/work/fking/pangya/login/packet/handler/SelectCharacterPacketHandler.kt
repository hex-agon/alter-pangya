package work.fking.pangya.login.packet.handler

import io.netty.buffer.ByteBuf
import work.fking.pangya.login.LoginServer
import work.fking.pangya.login.Player
import work.fking.pangya.login.net.ClientPacketHandler
import work.fking.pangya.login.net.LoginState

class SelectCharacterPacketHandler : ClientPacketHandler {
    override fun handle(server: LoginServer, player: Player, packet: ByteBuf) {

        if (player.loginState() != LoginState.SELECTING_CHARACTER) {
            throw IllegalStateException("Unexpected state ${player.loginState()}, expected SELECTING_CHARACTER")
        }
        player.setLoginState(LoginState.AUTHENTICATED)
    }
}
