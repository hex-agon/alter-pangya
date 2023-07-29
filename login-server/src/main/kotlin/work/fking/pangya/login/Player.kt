package work.fking.pangya.login

import io.netty.channel.Channel
import work.fking.pangya.login.auth.UserInfo
import work.fking.pangya.login.net.LoginState
import work.fking.pangya.login.net.LoginState.AUTHENTICATED

class Player(
    private val channel: Channel,
    val uid: Int,
    val loginKey: String = Rand.randomHexString(16),
    val sessionKey: String = Rand.randomHexString(16),
    val username: String,
    var nickname: String?,
    val hasBaseCharacter: Boolean = false
) {
    var state: LoginState = AUTHENTICATED
        set(value) {
            require(state.validTransition(value)) { "Invalid loginState transition, from $state to $value" }
            field = value
        }

    // these values are passed through the SessionInfo for the Game Server to create the desired starting character.
    // It is done like this to avoid having to copy over character creation logic from the game server code base.
    var pickedCharacterIffId: Int? = null
    var pickedCharacterHairColor: Int? = null

    constructor(channel: Channel, userInfo: UserInfo) : this(
        channel = channel,
        uid = userInfo.uid,
        username = userInfo.username,
        nickname = userInfo.nickname,
        hasBaseCharacter = userInfo.hasBaseCharacter
    )

    fun write(message: Any) {
        channel.write(message, channel.voidPromise())
    }

    fun writeAndFlush(message: Any) {
        channel.writeAndFlush(message)
    }

    fun flush() {
        channel.flush()
    }
}
