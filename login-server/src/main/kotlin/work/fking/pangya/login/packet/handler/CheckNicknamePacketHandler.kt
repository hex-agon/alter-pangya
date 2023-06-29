package work.fking.pangya.login.packet.handler

import io.netty.buffer.ByteBuf
import work.fking.pangya.login.LoginServer
import work.fking.pangya.login.Player
import work.fking.pangya.login.net.ClientPacketHandler
import work.fking.pangya.login.net.LoginState
import work.fking.pangya.login.packet.outbound.CheckNicknameReplies
import work.fking.pangya.networking.protocol.readPString

class CheckNicknamePacketHandler : ClientPacketHandler {

    override fun handle(server: LoginServer, player: Player, packet: ByteBuf) {
        val nickname = packet.readPString()

        if (player.loginState() !in arrayOf(LoginState.SELECTING_NICKNAME, LoginState.SELECTED_NICKNAME)) {
            throw IllegalStateException("Unexpected state ${player.loginState()}, expected SELECTING_NICKNAME or SELECTED_NICKNAME")
        }
        player.channel.writeAndFlush(CheckNicknameReplies.available(nickname))
    }
}
