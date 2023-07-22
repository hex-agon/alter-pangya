package work.fking.pangya.game

import com.fasterxml.jackson.dataformat.toml.TomlMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import org.apache.logging.log4j.LogManager
import work.fking.pangya.discovery.DiscoveryClient
import work.fking.pangya.discovery.HeartbeatPublisher
import work.fking.pangya.discovery.ServerType.GAME
import work.fking.pangya.game.persistence.PersistenceContext
import java.nio.file.Files
import java.nio.file.Path


object Bootstrap {
    private val LOGGER = LogManager.getLogger(Bootstrap::class.java)

    @JvmStatic
    fun main(args: Array<String>) {
        LOGGER.info("Bootstrapping the game server server...")
        val objectMapper = TomlMapper().registerKotlinModule()

        val serverConfig = objectMapper.readValue<GameServerConfig>(Files.newInputStream(Path.of("config.toml")))
        val redisClient = RedisClient.create(RedisURI.create(System.getenv("REDIS_URI")))
        val discoveryClient = DiscoveryClient(redisClient)
        val sessionClient = SessionClient(redisClient)

        val server = GameServer(
            serverConfig = serverConfig,
            persistenceContext = PersistenceContext(),
            sessionClient = sessionClient
        )
        HeartbeatPublisher(discoveryClient, GAME, serverConfig) { server.players.count() }.start()
        server.start()
    }
}
