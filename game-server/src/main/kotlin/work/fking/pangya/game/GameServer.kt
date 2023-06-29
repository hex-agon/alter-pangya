package work.fking.pangya.game

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelOption
import org.slf4j.LoggerFactory
import work.fking.pangya.common.server.ServerConfig
import work.fking.pangya.game.net.ServerChannelInitializer
import work.fking.pangya.game.player.Item
import work.fking.pangya.game.player.Player
import work.fking.pangya.networking.selectBestEventLoopAvailable
import work.fking.pangya.networking.selectBestServerChannelAvailable
import java.net.InetAddress
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

class GameServer(
    private val serverConfig: ServerConfig,
    private val sessionClient: SessionClient,
    private val serverChannels: List<ServerChannel>
) {
    private val logger = LoggerFactory.getLogger(GameServer::class.java)

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
            if (serverChannel.id() == id) {
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

    fun registerPlayer(channel: Channel, uid: Int, nickname: String): Player {
        val player = Player(channel, uid, connectionIdSequence.incrementAndGet(), nickname)
        val characterRoster = player.characterRoster()
        characterRoster.unlockCharacter(67108872)
        val caddieRoster = player.caddieRoster()
        caddieRoster.unlockCaddie(469762048)
        val inventory = player.inventory()
        inventory.add(Item(iffId = 268435511)) // clubset
        inventory.add(Item(iffId = 335544382, quantity = 100)) // comets
        inventory.add(Item(iffId = 436207656, quantity = 100)) // papel shop coupons

        // lucia items
        inventory.add(Item(iffId = 136331265))
        inventory.add(Item(iffId = 136423465))
        inventory.add(Item(iffId = 136456205))
        inventory.add(Item(iffId = 136456192))
        val equipment = player.equipment()

        characterRoster.findByIffId(67108872)?.let(equipment::equipCharacter)

        caddieRoster.findByIffId(469762048)?.let(equipment::equipCaddie)
        inventory.findByIffId(268435511)?.let(equipment::equipClubSet)
        inventory.findByIffId(335544382)?.let(equipment::equipComet)

        players[player.connectionId()] = player
        playerCount.incrementAndGet()
        channel.closeFuture().addListener { onPlayerDisconnect(player) }
        return player
    }

    private fun onPlayerDisconnect(player: Player) {
        val channel = player.currentChannel()
        channel?.removePlayer(player)
        players.remove(player.connectionId())
        playerCount.decrementAndGet()
        logger.debug("{} disconnected", player.nickname())
    }

    fun start() {
        val bootstrap = ServerBootstrap()
        val bossGroup = selectBestEventLoopAvailable()
        val workerGroup = selectBestEventLoopAvailable(2)

        bootstrap.group(bossGroup, workerGroup)
            .channel(selectBestServerChannelAvailable())
            .childOption(ChannelOption.TCP_NODELAY, true)
            .childHandler(ServerChannelInitializer(this))

        logger.info("Binding to port {}...", serverConfig.port)
        try {
            val inetAddress = InetAddress.getByName(serverConfig.bindAddress)
            val channel = bootstrap.bind(inetAddress, serverConfig.port)
                .sync()
                .channel()
            logger.info("Successfully bound to port {}, server bootstrap completed!", serverConfig.port)
            channel.closeFuture().sync()
        } finally {
            bossGroup.shutdownGracefully()
            workerGroup.shutdownGracefully()
        }
    }
}
