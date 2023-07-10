package work.fking.pangya.login

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelOption
import org.slf4j.LoggerFactory
import work.fking.pangya.discovery.DiscoveryClient
import work.fking.pangya.login.auth.Authenticator
import work.fking.pangya.login.auth.SessionClient
import work.fking.pangya.login.auth.UserInfo
import work.fking.pangya.login.net.ServerChannelInitializer
import work.fking.pangya.networking.selectBestEventLoopAvailable
import work.fking.pangya.networking.selectBestServerChannelAvailable
import java.net.InetAddress
import java.util.concurrent.Executors

private val LOGGER = LoggerFactory.getLogger(LoginServer::class.java)

class LoginServer(
    val discoveryClient: DiscoveryClient,
    val serverConfig: LoginServerConfig,
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

    private fun onPlayerDisconnect(player: Player) {
        LOGGER.debug("{} disconnected", player.username)
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

        LOGGER.info("Binding to port {}...", serverConfig.port)
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
