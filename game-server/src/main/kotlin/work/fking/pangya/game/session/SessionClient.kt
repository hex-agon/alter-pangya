package work.fking.pangya.game.session

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.lettuce.core.RedisClient
import io.lettuce.core.pubsub.api.reactive.ChannelMessage
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.player.Player

class SessionClient(
    redisClient: RedisClient,
    private val gameServer: GameServer
) {
    private val redis = redisClient.connect().sync()
    private val pubSub = redisClient.connectPubSub().sync()

    private val objectMapper = jacksonObjectMapper()

    init {
        val subConnection = redisClient.connectPubSub()
        val reactive = subConnection.reactive()
        reactive.subscribe("sessions.inquiry").subscribe()

        reactive.observeChannels()
            .doOnNext { onServerInquiry(it) }
            .subscribe()
    }

    private fun onServerInquiry(message: ChannelMessage<String, String>) {
        val sessionKey = message.message
        gameServer.players.find { player -> player.sessionKey == sessionKey } ?: return

        val response = object {
            val active = true
            val serverId = gameServer.serverConfig.id
            val sessionKey = sessionKey
        }
        pubSub.publish("sessions.inquiry.response", objectMapper.writeValueAsString(response))
    }

    fun unregisterSession(player: Player) {
        redis.del("session-${player.username}")
    }

    fun loadSessionInfo(loginKey: String): SessionInfo? {
        val json = redis[loginKey] ?: return null
        return objectMapper.readValue<SessionInfo>(json)
    }

}