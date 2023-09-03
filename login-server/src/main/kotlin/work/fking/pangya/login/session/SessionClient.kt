package work.fking.pangya.login.session

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.lettuce.core.RedisClient
import io.lettuce.core.api.sync.RedisCommands
import work.fking.pangya.login.Player

class SessionClient(
    private val redis: RedisCommands<String, String>
) {
    private val objectMapper = jacksonObjectMapper()

    constructor(redisClient: RedisClient) : this(redisClient.connect().sync())

    fun sessionKeyForUsername(username: String): String? {
        return redis["session-$username"]
    }

    fun registerSession(player: Player) {
        redis["session-${player.username}"] = player.sessionKey
    }

    fun unregisterSession(player: Player) {
        redis.del("session-${player.username}")
    }

    fun registerHandoverInfo(player: Player, serverId: Int) {
        val nickname = player.nickname
        requireNotNull(nickname) { "Cannot register session for ${player.username} because it doesn't have a nickname set" }

        val userInfo = HandoverInfo(
            targetServerId = serverId,
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
}