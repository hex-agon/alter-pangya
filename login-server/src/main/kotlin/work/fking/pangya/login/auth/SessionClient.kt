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

    fun registerSession(player: Player, serverId: Int) {
        val nickname = player.nickname
        requireNotNull(nickname) { "Cannot register session for ${player.username} because it doesn't have a nickname set" }

        val userInfo = SessionInfo(
            targetServerId = serverId,
            sessionKey = player.sessionKey,
            uid = player.uid,
            username = player.username,
            nickname = nickname,
            characterIffId = player.pickedCharacterIffId,
            characterHairColor = player.pickedCharacterHairColor
        )
        redisCommands[player.sessionKey] = objectMapper.writeValueAsString(userInfo)
    }

    fun unregisterSession(player: Player) {
        // This is called once the player disconnects from the login server, then,
        // the game server has around 10 seconds to retrieve the session before it expires
        redisCommands.expire(player.sessionKey, 10)
    }

    private data class SessionInfo(
        val targetServerId: Int,
        val sessionKey: String,
        val uid: Int,
        val username: String,
        val nickname: String,
        val characterIffId: Int?,
        val characterHairColor: Int?
    )
}