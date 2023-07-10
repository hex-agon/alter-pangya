package work.fking.pangya.discovery

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.lettuce.core.RedisClient
import io.lettuce.core.pubsub.api.reactive.ChannelMessage
import io.lettuce.core.pubsub.api.sync.RedisPubSubCommands
import org.apache.logging.log4j.LogManager
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

private val LOGGER = LogManager.getLogger(DiscoveryClient::class.java)
private const val CHANNEL_NAME = "pangya.servers.heartbeat"

class DiscoveryClient(
    redisClient: RedisClient,
    private val redis: RedisPubSubCommands<String, String> = redisClient.connectPubSub().sync()
) {
    private val objectMapper = jacksonObjectMapper()

    init {
        val subConnection = redisClient.connectPubSub()
        val reactive = subConnection.reactive()
        reactive.subscribe(CHANNEL_NAME).subscribe()

        reactive.observeChannels()
            .filter { it.channel == CHANNEL_NAME }
            .doOnNext { onChannelMessage(it) }
            .subscribe()
    }

    private val serversLock = ReentrantLock()
    private val knownServers: MutableList<KnownServer> = ArrayList()

    /**
     * Retrieves all the instances of the requested server type.
     *
     * @return A list containing all the found servers.
     */
    fun instances(serverType: ServerType): List<ServerInfo> {
        synchronized(knownServers) {
            cleanup()
            return knownServers.map { it.info }
                .filter { it.type == serverType }
        }
    }

    /**
     * Synchronously publishes the server info to be discovered by other servers.
     *
     * @param serverInfo The server info to be published.
     */
    fun publish(serverInfo: ServerInfo?) {
        try {
            val serializedServerInfo = objectMapper.writeValueAsString(serverInfo)
            redis.publish(CHANNEL_NAME, serializedServerInfo)
        } catch (e: JsonProcessingException) {
            LOGGER.warn("Failed to encode ServerInfo", e)
        }
    }

    private fun onChannelMessage(message: ChannelMessage<String, String>) {
        try {
            val serverInfo = objectMapper.readValue<ServerInfo>(message.message)
            register(serverInfo)
        } catch (e: JsonProcessingException) {
            LOGGER.warn("Failed to decode ServerInfo message", e)
        }
    }

    private fun register(info: ServerInfo) {
        val knownServer = KnownServer(LocalDateTime.now(), info)
        synchronized(knownServers) {
            val idx = indexByServerId(info.id)
            if (idx == -1) {
                LOGGER.trace("New server discovered, {} @ {}:{}", info.name, info.ip, info.port)
                knownServers.add(knownServer)
            } else {
                LOGGER.trace("Updating known server, {} @ {}:{}", info.name, info.ip, info.port)
                knownServers.set(idx, knownServer)
            }
        }
    }

    private fun indexByServerId(serverId: Int): Int {
        return knownServers.indexOfFirst { it.info.id == serverId }
    }

    private fun cleanup() {
        val now = LocalDateTime.now()
        serversLock.withLock {
            knownServers.removeIf { ChronoUnit.MINUTES.between(it.knownSince, now) > 3 }
        }
    }
}

private class KnownServer(
    val knownSince: LocalDateTime,
    val info: ServerInfo
)