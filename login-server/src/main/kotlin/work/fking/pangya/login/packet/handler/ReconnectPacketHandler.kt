package work.fking.pangya.login.packet.handler

import io.netty.buffer.ByteBuf
import work.fking.pangya.login.LoginServer
import work.fking.pangya.login.Player
import work.fking.pangya.login.net.ClientPacketHandler
import work.fking.pangya.login.packet.outbound.LoginReplies
import work.fking.pangya.networking.protocol.readPString

class ReconnectPacketHandler : ClientPacketHandler {
    override fun handle(server: LoginServer, player: Player, packet: ByteBuf) {
        val username = packet.readPString()
        val playerUid = packet.readIntLE()
        val loginKey = packet.readPString()

        player.channel.writeAndFlush(error(LoginReplies.Error.INVALID_ID_PW))
    }
}
