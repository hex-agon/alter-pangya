package work.fking.pangya.game

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.lettuce.core.RedisClient
import io.lettuce.core.api.sync.RedisCommands
import work.fking.pangya.game.player.Player

class SessionClient(
    private val redis: RedisCommands<String, String>
) {
    constructor(redisClient: RedisClient) : this(redisClient.connect().sync())

    private val objectMapper = jacksonObjectMapper()

    fun unregisterSession(player: Player) {
        redis.del("session-${player.username}")
    }

    fun loadHandoverInfo(loginKey: String): HandoverInfo? {
        val json = redis[loginKey] ?: return null
        return objectMapper.readValue<HandoverInfo>(json)
    }

    @JvmRecord
    data class HandoverInfo(
        val targetServerId: Int,
        val loginKey: String,
        val uid: Int,
        val username: String,
        val nickname: String,
        val characterIffId: Int?,
        val characterHairColor: Int?
    )
}
