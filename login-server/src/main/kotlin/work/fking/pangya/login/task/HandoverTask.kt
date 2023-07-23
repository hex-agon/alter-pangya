package work.fking.pangya.login.task

import org.slf4j.LoggerFactory
import work.fking.pangya.login.LoginServer
import work.fking.pangya.login.Player
import work.fking.pangya.login.packet.outbound.LoginReplies

private val LOGGER = LoggerFactory.getLogger(HandoverTask::class.java)

class HandoverTask(
    private val server: LoginServer,
    private val player: Player,
    private val serverId: Int
) : Runnable {

    override fun run() {
        LOGGER.info("Player {} is being handed over to serverId={} with loginKey={} and sessionKey={}", player.uid, serverId, player.loginKey, player.sessionKey)
        runCatching { server.sessionClient.registerSession(player, serverId) }.onFailure { throw RuntimeException("Failed to register player session") }
        player.writeAndFlush(LoginReplies.sessionKey(player.sessionKey))
    }
}