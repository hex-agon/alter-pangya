package work.fking.pangya.login

import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import org.apache.logging.log4j.LogManager
import work.fking.pangya.common.server.ServerConfigLoader
import work.fking.pangya.discovery.DiscoveryClient
import work.fking.pangya.discovery.HeartbeatPublisher
import work.fking.pangya.discovery.ServerType.LOGIN
import work.fking.pangya.login.auth.Authenticator.Companion.NOOP_AUTHENTICATOR
import work.fking.pangya.login.auth.SessionClient

object Bootstrap {
    private val LOGGER = LogManager.getLogger(Bootstrap::class.java)

    @JvmStatic
    fun main(args: Array<String>) {
        LOGGER.info("Bootstrapping the login server...")
        val redisClient = RedisClient.create(RedisURI.create(System.getenv("REDIS_URI")))
        val discoveryClient = DiscoveryClient(redisClient)
        val sessionClient = SessionClient(redisClient)
        val serverConfig = ServerConfigLoader.load("config.toml")
        val loginServer = LoginServer(discoveryClient, serverConfig, sessionClient, NOOP_AUTHENTICATOR)
        LOGGER.debug("Initializing service discovery...")
        HeartbeatPublisher(discoveryClient, LOGIN, serverConfig).start()
        LOGGER.debug("Starting the login server...")
        loginServer.start()
    }
}
