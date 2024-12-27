package work.fking.pangya.login.session

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.lettuce.core.RedisClient
import io.lettuce.core.pubsub.api.reactive.ChannelMessage
import org.slf4j.LoggerFactory
import work.fking.pangya.login.Player
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class SessionClient(
    redisClient: RedisClient
) {
    private val logger = LoggerFactory.getLogger(SessionClient::class.java)
    private val redis = redisClient.connect().sync()
    private val pubSub = redisClient.connectPubSub().sync()

    private val objectMapper = jacksonObjectMapper().registerKotlinModule()

    private val pendingSessionInquiries = ConcurrentHashMap<String, CompletableFuture<SessionInquiryResponse>>()

    init {
        val subConnection = redisClient.connectPubSub()
        val reactive = subConnection.reactive()
        reactive.subscribe("sessions.inquiry.response").subscribe()

        reactive.observeChannels()
            .doOnNext { onServerInquiryResponse(it) }
            .subscribe()
    }

    private fun sessionKey(player: Player) = sessionKey(player.username)

    private fun sessionKey(username: String) = "session-${username}"

    fun registerSession(player: Player) {
        val key = sessionKey(player)
        redis[key] = player.sessionKey
    }

    fun unregisterSession(player: Player) {
        val key = sessionKey(player)
        redis.del(key)
    }

    fun findSessionByUsername(username: String): FindSessionResult {
        val key = sessionKey(username)
        val sessionId = redis[key] ?: return NotFound

        logger.debug("Inquiring existing session for username {} with key {}", username, sessionId)
        // if found, query other servers
        val response = CompletableFuture<SessionInquiryResponse>()
        pubSub.publish("sessions.inquiry", sessionId)

        try {
            // wait at most 1 second for the future to complete
            val inquiryResponse = response.get(1, TimeUnit.SECONDS)

            logger.debug("Got response for user session {} with {}", username, inquiryResponse)
            return if (inquiryResponse.active) {
                Active(inquiryResponse.serverId)
            } else {
                NotFound
            }
        } catch (e: Exception) {
            return when (e) {
                is TimeoutException -> Timeout
                else -> Error(e)
            }
        }
    }

    fun registerHandoverInfo(player: Player) {
        val nickname = player.nickname
        requireNotNull(nickname) { "Cannot register session for ${player.username} because it doesn't have a nickname set" }

        val userInfo = HandoverInfo(
            sessionKey = player.sessionKey,
            loginKey = player.loginKey,
            uid = player.uid,
            username = player.username,
            nickname = nickname,
            characterIffId = player.pickedCharacterIffId,
            characterHairColor = player.pickedCharacterHairColor
        )
        redis[player.loginKey] = objectMapper.writeValueAsString(userInfo)
    }

    fun expireHandoverInfo(player: Player) {
        // This is called once the player disconnects from the login server, then,
        // the game server has around 10 seconds to retrieve the session before it expires
        redis.expire(player.loginKey, 10)
    }

    private fun onServerInquiryResponse(message: ChannelMessage<String, String>) {
        logger.debug("Incoming session inquiry response {}", message)
        val inquiryResponse = objectMapper.readValue<SessionInquiryResponse>(message.message)
        val future = pendingSessionInquiries.get(inquiryResponse.sessionId) ?: return

        future.complete(inquiryResponse)
    }
}