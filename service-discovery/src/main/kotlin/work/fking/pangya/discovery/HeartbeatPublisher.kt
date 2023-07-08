package work.fking.pangya.discovery

import org.apache.logging.log4j.LogManager
import work.fking.pangya.common.server.ServerBoost
import work.fking.pangya.common.server.ServerConfig
import work.fking.pangya.common.server.ServerFlag
import work.fking.pangya.common.server.ServerIcon
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.function.IntSupplier

private val LOGGER = LogManager.getLogger(HeartbeatPublisher::class.java)

class HeartbeatPublisher(
    private val discoveryClient: DiscoveryClient,
    serverType: ServerType,
    serverConfig: ServerConfig,
    private val playerCountSupplier: IntSupplier = IntSupplier { 0 },
) {
    private val staticInfo = StaticInfo(
        serverType,
        serverConfig.id,
        serverConfig.name,
        serverConfig.capacity,
        serverConfig.advertiseAddress,
        serverConfig.port,
        serverConfig.flags,
        serverConfig.boosts,
        serverConfig.icon
    )

    private val executorService = Executors.newSingleThreadScheduledExecutor { runnable: Runnable? ->
        val thread = Thread(runnable)
        thread.isDaemon = true
        thread.name = "HeartbeatPublisher"
        thread
    }

    fun start() {
        executorService.scheduleWithFixedDelay(this::publish, 0, 1, TimeUnit.MINUTES)
    }

    private fun publish() {
        val serverInfo = ServerInfo(
            staticInfo.type,
            staticInfo.id,
            staticInfo.name,
            staticInfo.capacity,
            playerCountSupplier.asInt,
            staticInfo.ip,
            staticInfo.port,
            staticInfo.flags,
            staticInfo.boosts,
            staticInfo.icon
        )
        try {
            discoveryClient.publish(serverInfo)
        } catch (e: Exception) {
            LOGGER.warn("Failed to publish heartbeat", e)
        }
    }
}

private data class StaticInfo(
    val type: ServerType,
    val id: Int,
    val name: String,
    val capacity: Int,
    val ip: String,
    val port: Int,
    val flags: List<ServerFlag>,
    val boosts: List<ServerBoost>,
    val icon: ServerIcon
)
