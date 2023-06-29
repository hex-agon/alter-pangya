package work.fking.pangya.login.task

import io.netty.channel.Channel
import org.slf4j.LoggerFactory
import work.fking.pangya.discovery.ServerType
import work.fking.pangya.login.LoginServer
import work.fking.pangya.login.auth.UserInfo
import work.fking.pangya.login.net.ClientPacketDispatcher
import work.fking.pangya.login.net.ClientPacketType
import work.fking.pangya.login.net.ClientProtocol
import work.fking.pangya.login.net.GameProtocolDecoder
import work.fking.pangya.login.net.LoginState
import work.fking.pangya.login.packet.outbound.LoginReplies
import work.fking.pangya.login.packet.outbound.ServerListReplies

class LoginTask(
    private val loginServer: LoginServer,
    private val channel: Channel,
    private val cryptKey: Int,
    private val username: String,
    private val password: CharArray
) : Runnable {

    companion object {
        @JvmStatic
        private val PROTOCOL: ClientProtocol = ClientProtocol(ClientPacketType.values())

        @JvmStatic
        private val LOGGER = LoggerFactory.getLogger(LoginTask::class.java)
    }

    override fun run() {
        val userInfo: UserInfo? = try {
            loginServer.authenticator.authenticate(username, password)
        } catch (e: Exception) {
            LOGGER.warn("Authentication failed", e)
            channel.writeAndFlush(LoginReplies.error(LoginReplies.Error.INCORRECT_USERNAME_PASSWORD))
            return
        }
        val player = loginServer.registerPlayer(channel, userInfo)
        val discoveryClient = loginServer.discoveryClient
        val gameServers = discoveryClient.instances(ServerType.GAME)
        val socialServers = discoveryClient.instances(ServerType.SOCIAL)

        val pipeline = channel.pipeline()
        pipeline.remove("loginHandler")
        pipeline.addLast("decoder", GameProtocolDecoder(PROTOCOL, cryptKey))
        pipeline.addLast("packetDispatcher", ClientPacketDispatcher(loginServer, player, PROTOCOL.handlers()))

        channel.write(LoginReplies.loginKey(player.loginKey))
        channel.write(LoginReplies.chatMacros())
        channel.write(LoginReplies.success(player.uid, player.username, player.nickname))
        channel.write(ServerListReplies.gameServers(gameServers))
        channel.write(ServerListReplies.socialServers(socialServers))
        player.setLoginState(LoginState.LOGGED_IN)
        channel.flush()
    }
}
