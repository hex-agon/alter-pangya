package work.fking.pangya.login.task

import io.netty.channel.Channel
import org.slf4j.LoggerFactory
import work.fking.pangya.login.LoginServer
import work.fking.pangya.login.auth.ExceptionResult
import work.fking.pangya.login.auth.InvalidCredentialsResult
import work.fking.pangya.login.auth.SuccessResult
import work.fking.pangya.login.auth.UserInfo
import work.fking.pangya.login.net.ClientPacketDispatcher
import work.fking.pangya.login.net.ClientPacketType
import work.fking.pangya.login.net.ClientProtocol
import work.fking.pangya.login.net.LoginState.LOGGED_IN
import work.fking.pangya.login.net.LoginState.SELECTING_CHARACTER
import work.fking.pangya.login.net.LoginState.SELECTING_NICKNAME
import work.fking.pangya.login.net.pipe.ProtocolDecoder
import work.fking.pangya.login.packet.outbound.LoginReplies

private val PROTOCOL: ClientProtocol = ClientProtocol(ClientPacketType.values())
private val LOGGER = LoggerFactory.getLogger(LoginTask::class.java)

class LoginTask(
    private val loginServer: LoginServer,
    private val channel: Channel,
    private val cryptKey: Int,
    private val username: String,
    private val password: CharArray
) : Runnable {

    override fun run() {
        when (val authResult = loginServer.authenticator.authenticate(username, password)) {
            is SuccessResult -> onSuccessAuth(authResult.userInfo)
            is InvalidCredentialsResult -> onInvalidCredentials()
            is ExceptionResult -> {
                LOGGER.warn("Auth failed", authResult.exception)
            }
        }
    }

    private fun onSuccessAuth(userInfo: UserInfo) {
        val player = loginServer.registerPlayer(channel, userInfo)

        val pipeline = channel.pipeline()
        pipeline.remove("loginHandler")
        pipeline.remove("timeoutHandler")
        pipeline.addLast("decoder", ProtocolDecoder(PROTOCOL, cryptKey))
        pipeline.addLast("packetDispatcher", ClientPacketDispatcher(loginServer, player, PROTOCOL.handlers()))

        val nickname = player.nickname
        player.state = if (nickname == null) {
            channel.write(LoginReplies.createNickname())
            SELECTING_NICKNAME
        } else if (!player.hasBaseCharacter) {
            channel.write(LoginReplies.selectCharacter())
            SELECTING_CHARACTER
        } else {
            loginServer.proceedPlayerToLoggedIn(player)
            LOGGED_IN
        }
        channel.flush()
    }

    private fun onInvalidCredentials() {
        channel.writeAndFlush(LoginReplies.error(LoginReplies.Error.INCORRECT_USERNAME_PASSWORD))
    }
}
