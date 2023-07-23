package work.fking.pangya.login.packet.handler

import io.netty.buffer.ByteBuf
import work.fking.pangya.login.LoginServer
import work.fking.pangya.login.Player
import work.fking.pangya.login.net.ClientPacketHandler
import work.fking.pangya.login.net.LoginState.SELECTED_NICKNAME
import work.fking.pangya.login.net.LoginState.SELECTING_NICKNAME
import work.fking.pangya.login.packet.outbound.CheckNicknameReplies
import work.fking.pangya.networking.protocol.readPString

class CheckNicknamePacketHandler : ClientPacketHandler {

    override fun handle(server: LoginServer, player: Player, packet: ByteBuf) {
        val nickname = packet.readPString()

        require(player.state == SELECTING_NICKNAME || player.state == SELECTED_NICKNAME) {
            "Unexpected state ${player.state}, expected SELECTING_NICKNAME or SELECTED_NICKNAME"
        }
        // TODO: validate if nickname is unique
        player.state = SELECTED_NICKNAME
        player.writeAndFlush(CheckNicknameReplies.available(nickname))
    }
}
