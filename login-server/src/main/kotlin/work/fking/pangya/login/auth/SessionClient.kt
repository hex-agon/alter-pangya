package work.fking.pangya.login.auth

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.lettuce.core.RedisClient
import io.lettuce.core.api.sync.RedisCommands
import work.fking.pangya.login.Player

class SessionClient(
    private val redisCommands: RedisCommands<String, String>
) {
    private val objectMapper = jacksonObjectMapper()

    constructor(redisClient: RedisClient) : this(redisClient.connect().sync())

    fun registerSession(player: Player) {
        val userInfo = SessionInfo(player.sessionKey, player.uid, player.username, player.nickname)
        redisCommands[player.sessionKey] = objectMapper.writeValueAsString(userInfo)
    }

    fun unregisterSession(player: Player) {
        redisCommands.expire(player.sessionKey, 10)
    }

    private data class SessionInfo(
        val sessionKey: String,
        val uid: Int,
        val username: String,
        val nickname: String
    )
}