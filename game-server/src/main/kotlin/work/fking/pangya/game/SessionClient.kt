package work.fking.pangya.game

import com.fasterxml.jackson.databind.ObjectMapper
import io.lettuce.core.RedisClient
import io.lettuce.core.api.sync.RedisCommands

class SessionClient(
    private val redisCommands: RedisCommands<String, String>
) {
    constructor(redisClient: RedisClient) : this(redisClient.connect().sync())

    private val objectMapper = ObjectMapper()

    fun loadSession(sessionKey: String): SessionInfo? {
        val json = redisCommands[sessionKey] ?: return null
        return objectMapper.readValue(json, SessionInfo::class.java)
    }

    @JvmRecord
    data class SessionInfo(
        val sessionKey: String,
        val uid: Int,
        val username: String,
        val nickname: String
    )
}
