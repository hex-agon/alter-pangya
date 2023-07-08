package work.fking.pangya.login.auth

import work.fking.pangya.common.Rand

val NOOP_AUTHENTICATOR: Authenticator = Authenticator { username, password -> UserInfo(Rand.max(10000), username, username) }

fun interface Authenticator {
    fun authenticate(username: String, password: CharArray): UserInfo?
}
