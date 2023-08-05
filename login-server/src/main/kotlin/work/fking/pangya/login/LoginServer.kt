package work.fking.pangya.login

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelOption
import org.slf4j.LoggerFactory
import work.fking.pangya.discovery.DiscoveryClient
import work.fking.pangya.discovery.ServerType
import work.fking.pangya.login.auth.Authenticator
import work.fking.pangya.login.auth.SessionClient
import work.fking.pangya.login.auth.UserInfo
import work.fking.pangya.login.net.pipe.ServerChannelInitializer
import work.fking.pangya.login.packet.outbound.LoginReplies
import work.fking.pangya.login.packet.outbound.ServerListReplies
import work.fking.pangya.networking.selectBestEventLoopAvailable
import work.fking.pangya.networking.selectBestServerChannelAvailable
import java.net.InetAddress
import java.util.concurrent.Executors

private val LOGGER = LoggerFactory.getLogger(LoginServer::class.java)

class LoginServer(
    private val discoveryClient: DiscoveryClient,
    private val serverConfig: LoginServerConfig,
    val sessionClient: SessionClient,
    val authenticator: Authenticator
) {

    private val executorService = Executors.newVirtualThreadPerTaskExecutor()

    fun submitTask(runnable: Runnable) {
        executorService.execute(runnable)
    }

    fun registerPlayer(channel: Channel, userInfo: UserInfo?): Player {
        val player = Player(channel, userInfo!!)
        channel.closeFuture().addListener { onPlayerDisconnect(player) }
        LOGGER.debug("{} logged in from {}", player.username, channel.remoteAddress())
        return player
    }

    fun proceedPlayerToLoggedIn(player: Player) {
        val nickname = player.nickname
        requireNotNull(nickname) { "Cannot proceed player to logged in, player doesn't have a nickname" }

        val gameServers = discoveryClient.instances(ServerType.GAME)
        val socialServers = discoveryClient.instances(ServerType.SOCIAL)

        player.write(LoginReplies.loginKey(player.loginKey))
        player.write(LoginReplies.chatMacros())
        player.write(LoginReplies.success(player.uid, player.username, nickname))
        player.write(ServerListReplies.gameServers(gameServers))
        player.write(ServerListReplies.socialServers(socialServers))
        player.flush()
    }

    private fun onPlayerDisconnect(player: Player) {
        LOGGER.info("{} disconnected", player.username)
        sessionClient.unregisterSession(player)
    }

    fun start() {
        val bootstrap = ServerBootstrap()
        val bossGroup = selectBestEventLoopAvailable()
        val workerGroup = selectBestEventLoopAvailable(2)

        bootstrap.group(bossGroup, workerGroup)
            .channel(selectBestServerChannelAvailable())
            .childOption(ChannelOption.TCP_NODELAY, true)
            .childHandler(ServerChannelInitializer(this))

        try {
            val inetAddress = InetAddress.getByName(serverConfig.bindAddress)
            val channel = bootstrap.bind(inetAddress, serverConfig.port)
                .sync()
                .channel()
            LOGGER.info("Successfully bound to {}, server bootstrap completed!", channel.localAddress())
            channel.closeFuture().sync()
        } finally {
            bossGroup.shutdownGracefully()
            workerGroup.shutdownGracefully()
        }
    }
}
