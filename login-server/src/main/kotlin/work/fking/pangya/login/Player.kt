package work.fking.pangya.login

import io.netty.channel.Channel
import work.fking.pangya.common.Rand
import work.fking.pangya.login.auth.UserInfo
import work.fking.pangya.login.net.LoginState

class Player(
    val channel: Channel,
    val uid: Int,
    val loginKey: String = Rand.randomHexString(16),
    val sessionKey: String = Rand.randomHexString(16),
    val username: String,
    val nickname: String,
    private var loginState: LoginState = LoginState.AUTHENTICATED,
) {

    constructor(channel: Channel, userInfo: UserInfo) : this(
        channel = channel,
        uid = userInfo.uid,
        username = userInfo.username,
        nickname = userInfo.nickname
    )

    fun loginState(): LoginState {
        return loginState
    }

    fun setLoginState(loginState: LoginState) {
        if (this.loginState.validTransition(loginState)) {
            this.loginState = loginState
        } else {
            throw IllegalStateException("Attempted to set loginState to " + loginState + " from " + this.loginState)
        }
    }
}
