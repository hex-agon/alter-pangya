package work.fking.pangya.login.auth

import work.fking.pangya.common.Rand

fun interface Authenticator {
    fun authenticate(username: String, password: CharArray): UserInfo?

    companion object {
        @JvmStatic
        val NOOP_AUTHENTICATOR: Authenticator = Authenticator { username, password -> UserInfo(Rand.max(10000), username, username) }
    }
}
