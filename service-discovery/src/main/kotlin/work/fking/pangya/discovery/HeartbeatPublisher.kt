package work.fking.pangya.discovery

import org.apache.logging.log4j.LogManager
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.function.IntSupplier

private val LOGGER = LogManager.getLogger(HeartbeatPublisher::class.java)

private val executorService = Executors.newSingleThreadScheduledExecutor { runnable: Runnable? ->
    val thread = Thread(runnable)
    thread.isDaemon = true
    thread.name = "HeartbeatPublisher"
    thread
}

class HeartbeatPublisher(
    private val discoveryClient: DiscoveryClient,
    private val serverType: ServerType,
    private val serverConfig: ServerConfig,
    private val playerCountSupplier: IntSupplier = IntSupplier { 0 },
) {

    fun start() {
        executorService.scheduleWithFixedDelay(this::publish, 0, 1, TimeUnit.MINUTES)
    }

    private fun publish() {
        val serverInfo = ServerInfo(
            serverType,
            serverConfig.id,
            serverConfig.name,
            serverConfig.capacity,
            playerCountSupplier.asInt,
            serverConfig.advertiseAddress,
            serverConfig.port,
            serverConfig.flags ?: emptyList(),
            serverConfig.boosts ?: emptyList(),
            serverConfig.icon ?: ServerIcon.BLACK_PAPEL
        )
        try {
            discoveryClient.publish(serverInfo)
        } catch (e: Exception) {
            LOGGER.warn("Failed to publish heartbeat", e)
        }
    }
}

@JvmRecord
data class ServerInfo(
    val type: ServerType,
    val id: Int,
    val name: String,
    val capacity: Int,
    val playerCount: Int,
    val ip: String,
    val port: Int,
    val flags: List<ServerFlag>,
    val boosts: List<ServerBoost>,
    val icon: ServerIcon
)

enum class ServerType {
    LOGIN,
    GAME,
    SOCIAL
}
