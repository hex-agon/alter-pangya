package work.fking.pangya.login.packet.handler

import io.netty.buffer.ByteBuf
import work.fking.pangya.login.LoginServer
import work.fking.pangya.login.Player
import work.fking.pangya.login.net.ClientPacketHandler
import work.fking.pangya.login.net.LoginState.LOGGED_IN
import work.fking.pangya.login.net.LoginState.SELECTING_CHARACTER
import work.fking.pangya.login.net.LoginState.SELECTING_NICKNAME
import work.fking.pangya.login.packet.outbound.ConfirmCharacterReplies
import work.fking.pangya.login.packet.outbound.LoginReplies

class SelectCharacterPacketHandler : ClientPacketHandler {
    override fun handle(server: LoginServer, player: Player, packet: ByteBuf) {
        require(player.state == SELECTING_CHARACTER) { "Unexpected state ${player.state}, expected SELECTING_CHARACTER" }

        val characterIffId = packet.readIntLE()
        val hairColor = packet.readShortLE()
        player.pickedCharacterIffId = characterIffId
        player.pickedCharacterHairColor = hairColor.toInt()

        player.state = if (player.nickname == null) {
            player.writeAndFlush(LoginReplies.createNickname())
            SELECTING_NICKNAME
        } else {
            player.write(ConfirmCharacterReplies.ok())
            server.proceedPlayerToLoggedIn(player)
            LOGGED_IN
        }
    }
}
