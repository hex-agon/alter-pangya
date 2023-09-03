package work.fking.pangya.login

import com.fasterxml.jackson.dataformat.toml.TomlMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import org.apache.logging.log4j.LogManager
import work.fking.pangya.discovery.DiscoveryClient
import work.fking.pangya.discovery.HeartbeatPublisher
import work.fking.pangya.discovery.ServerType.LOGIN
import work.fking.pangya.login.auth.DatabaseAuthenticator
import work.fking.pangya.login.session.SessionClient
import work.fking.pangya.login.persistence.AccountRepository
import java.nio.file.Files
import java.nio.file.Path

object Bootstrap {
    private val LOGGER = LogManager.getLogger(Bootstrap::class.java)

    @JvmStatic
    fun main(args: Array<String>) {
        val objectMapper = TomlMapper().registerKotlinModule()
        LOGGER.info("Bootstrapping the login server...")
        val serverConfig = objectMapper.readValue<LoginServerConfig>(Files.newInputStream(Path.of("config.toml")))

        val redisClient = RedisClient.create(RedisURI.create(serverConfig.redis.url))
        val discoveryClient = DiscoveryClient(redisClient)
        val sessionClient = SessionClient(redisClient)

        val accountRepository = setupAccountRepository(serverConfig.database)

        val loginServer = LoginServer(
            discoveryClient = discoveryClient,
            serverConfig = serverConfig,
            sessionClient = sessionClient,
            authenticator = DatabaseAuthenticator(accountRepository)
        )

        LOGGER.debug("Initializing service discovery...")
        HeartbeatPublisher(discoveryClient, LOGIN, serverConfig).start()

        LOGGER.debug("Starting the login server...")
        loginServer.start()
    }

    private fun setupAccountRepository(databaseConfig: DatabaseConfig): AccountRepository {
        val hikariConfig = HikariConfig()

        with(hikariConfig) {
            driverClassName = "org.postgresql.Driver"
            jdbcUrl = databaseConfig.url
            username = databaseConfig.username
            password = databaseConfig.password
        }
        val dataSource = HikariDataSource(hikariConfig)

        return AccountRepository(dataSource)
    }
}
