package work.fking.pangya.game

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.lettuce.core.RedisClient
import io.lettuce.core.api.sync.RedisCommands

class SessionClient(
    private val redisCommands: RedisCommands<String, String>
) {
    constructor(redisClient: RedisClient) : this(redisClient.connect().sync())

    private val objectMapper = jacksonObjectMapper()

    fun loadSession(sessionKey: String): SessionInfo? {
        val json = redisCommands[sessionKey] ?: return null
        return objectMapper.readValue<SessionInfo>(json)
    }

    @JvmRecord
    data class SessionInfo(
        val sessionKey: String,
        val uid: Int,
        val username: String,
        val nickname: String
    )
}
