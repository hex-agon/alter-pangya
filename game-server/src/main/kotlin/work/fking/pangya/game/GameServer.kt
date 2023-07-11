package work.fking.pangya.game

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelOption
import org.slf4j.LoggerFactory
import work.fking.pangya.game.net.ServerChannelInitializer
import work.fking.pangya.game.player.Player
import work.fking.pangya.networking.selectBestEventLoopAvailable
import work.fking.pangya.networking.selectBestServerChannelAvailable
import java.net.InetAddress
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

private val LOGGER = LoggerFactory.getLogger(GameServer::class.java)

class GameServer(
    private val serverConfig: GameServerConfig,
    private val sessionClient: SessionClient,
    private val serverChannels: List<ServerChannel> = serverConfig.serverChannels
) {
    private val executorService = Executors.newVirtualThreadPerTaskExecutor()
    private val connectionIdSequence = AtomicInteger()
    private val playerCount = AtomicInteger()
    private val players: MutableMap<Int, Player> = ConcurrentHashMap()

    fun playerCount(): Int {
        return playerCount.get()
    }

    fun serverChannels(): List<ServerChannel> {
        return Collections.unmodifiableList(serverChannels)
    }

    fun serverChannelById(id: Int): ServerChannel? {
        for (serverChannel in serverChannels) {
            if (serverChannel.id == id) {
                return serverChannel
            }
        }
        return null
    }

    fun sessionClient(): SessionClient {
        return sessionClient
    }

    fun submitTask(runnable: Runnable) {
        executorService.execute(runnable)
    }

    fun findPlayer(connectionId: Int): Player? {
        return players[connectionId]
    }

    fun registerPlayer(channel: Channel, uid: Int, username: String, nickname: String): Player {
        val player = Player(channel, uid, connectionIdSequence.incrementAndGet(), username, nickname)
        giveTestStuff(player)

        players[player.connectionId] = player
        playerCount.incrementAndGet()
        channel.closeFuture().addListener { onPlayerDisconnect(player) }
        return player
    }

    private fun onPlayerDisconnect(player: Player) {
        val channel = player.currentChannel
        channel?.removePlayer(player)
        players.remove(player.connectionId)
        playerCount.decrementAndGet()
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
