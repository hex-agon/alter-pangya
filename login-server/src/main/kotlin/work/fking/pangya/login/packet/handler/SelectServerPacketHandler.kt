package work.fking.pangya.login.packet.handler

import io.netty.buffer.ByteBuf
import org.slf4j.LoggerFactory
import work.fking.pangya.login.LoginServer
import work.fking.pangya.login.Player
import work.fking.pangya.login.net.ClientPacketHandler
import work.fking.pangya.login.packet.outbound.LoginReplies

private val LOGGER = LoggerFactory.getLogger(SelectServerPacketHandler::class.java)

class SelectServerPacketHandler : ClientPacketHandler {

    override fun handle(server: LoginServer, player: Player, packet: ByteBuf) {
        val serverId = packet.readShortLE().toInt()
        LOGGER.info("Player {} is being handed over to serverId={} with loginKey={} and sessionKey={}", player.uid, serverId, player.loginKey, player.sessionKey)
        runCatching { server.sessionClient.registerSession(player) }.onFailure { throw RuntimeException("Failed to register player session") }
        player.channel.writeAndFlush(LoginReplies.sessionKey(player.sessionKey))
    }
}
