package work.fking.pangya.login.packet.handler

import io.netty.buffer.ByteBuf
import work.fking.pangya.discovery.ServerType
import work.fking.pangya.login.LoginServer
import work.fking.pangya.login.Player
import work.fking.pangya.login.net.ClientPacketHandler
import work.fking.pangya.login.net.LoginState.LOGGED_IN
import work.fking.pangya.login.net.LoginState.SELECTED_NICKNAME
import work.fking.pangya.login.net.LoginState.SELECTING_CHARACTER
import work.fking.pangya.login.packet.outbound.LoginReplies
import work.fking.pangya.login.packet.outbound.ServerListReplies
import work.fking.pangya.networking.protocol.readPString

class ConfirmNicknamePacketHandler : ClientPacketHandler {
    override fun handle(server: LoginServer, player: Player, packet: ByteBuf) {
        require(player.state == SELECTED_NICKNAME) { throw IllegalStateException("Unexpected state ${player.state}, expected SELECTED_NICKNAME") }

        val nickname = packet.readPString()
        player.nickname = nickname

        player.state = if (player.needCharacterSelect && player.pickedCharacterIffId == null) {
            player.writeAndFlush(LoginReplies.selectCharacter())
            SELECTING_CHARACTER
        } else {
            server.proceedPlayerToLoggedIn(player)
            LOGGED_IN
        }
    }
}
