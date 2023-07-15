package work.fking.pangya.game

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelOption
import org.slf4j.LoggerFactory
import work.fking.pangya.game.net.ServerChannelInitializer
import work.fking.pangya.game.player.Player
import work.fking.pangya.game.player.PlayerGroup
import work.fking.pangya.networking.selectBestEventLoopAvailable
import work.fking.pangya.networking.selectBestServerChannelAvailable
import java.net.InetAddress
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

private val LOGGER = LoggerFactory.getLogger(GameServer::class.java)

class GameServer(
    private val serverConfig: GameServerConfig,
    val sessionClient: SessionClient,
    val serverChannels: List<ServerChannel> = serverConfig.serverChannels
) {
    private val executorService = Executors.newVirtualThreadPerTaskExecutor()
    private val connectionIdSequence = AtomicInteger()
    val players = PlayerGroup()

    fun serverChannelById(id: Int): ServerChannel? {
        return serverChannels.firstOrNull { it.id == id }
    }

    fun submitTask(runnable: Runnable) {
        executorService.execute(runnable)
    }

    fun registerPlayer(channel: Channel, uid: Int, username: String, nickname: String): Player {
        val player = Player(
            channel = channel,
            uid = uid,
            connectionId = connectionIdSequence.incrementAndGet(),
            username = username,
            nickname = nickname,
        )
        giveTestStuff(player)

        players.add(player)
        channel.closeFuture().addListener { onPlayerDisconnect(player) }
        return player
    }

    private fun onPlayerDisconnect(player: Player) {
        val channel = player.currentChannel
        channel?.removePlayer(player)
        players.remove(player)
        LOGGER.debug("{} disconnected", player.nickname)
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
