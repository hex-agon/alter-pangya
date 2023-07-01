package work.fking.pangya.game

import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import org.apache.logging.log4j.LogManager
import work.fking.pangya.common.server.ServerConfigLoader
import work.fking.pangya.discovery.DiscoveryClient
import work.fking.pangya.discovery.HeartbeatPublisher
import work.fking.pangya.discovery.ServerType.GAME
import work.fking.pangya.game.ServerChannel.Restriction


object Bootstrap {
    private val LOGGER = LogManager.getLogger(Bootstrap::class.java)

    @JvmStatic
    fun main(args: Array<String>) {
        LOGGER.info("Bootstrapping the game server server...")
        val serverConfig = ServerConfigLoader.load("config.toml")
        val redisClient = RedisClient.create(RedisURI.create(System.getenv("REDIS_URI")))
        val discoveryClient = DiscoveryClient(redisClient)
        val sessionClient = SessionClient(redisClient)

        val serverChannels = listOf(
            ServerChannel(1, "Rookies", 20, listOf(Restriction.ROOKIES_ONLY)),
            ServerChannel(2, "Beginners & Juniors", 20, listOf(Restriction.BEGINNERS_AND_JUNIORS_ONLY)),
            ServerChannel(3, "Juniors & Seniors", 20, listOf(Restriction.JUNIORS_AND_SENIORS_ONLY)),
            ServerChannel(4, "Free", 20, listOf())
        )

        val server = GameServer(serverConfig, sessionClient, serverChannels)
        HeartbeatPublisher(discoveryClient, GAME, serverConfig) { server.playerCount() }.start()
        server.start()
    }
}
