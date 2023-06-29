package work.fking.pangya.login.packet.handler

import io.netty.buffer.ByteBuf
import work.fking.pangya.login.LoginServer
import work.fking.pangya.login.Player
import work.fking.pangya.login.net.ClientPacketHandler
import work.fking.pangya.login.net.LoginState
import work.fking.pangya.networking.protocol.readPString

class SetNicknamePacketHandler : ClientPacketHandler {
    override fun handle(server: LoginServer, player: Player, packet: ByteBuf) {
        val nickname = packet.readPString()

        if (player.loginState() != LoginState.SELECTED_NICKNAME) {
            throw IllegalStateException("Unexpected state ${player.loginState()}, expected SELECTED_NICKNAME")
        }
        player.setLoginState(LoginState.SELECTED_NICKNAME)
    }
}
