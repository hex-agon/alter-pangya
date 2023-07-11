package work.fking.pangya.login.auth

import java.util.concurrent.atomic.AtomicInteger

private val uidSequence = AtomicInteger(1)

val NOOP_AUTHENTICATOR: Authenticator = Authenticator { username, _ -> UserInfo(uidSequence.getAndIncrement(), username, username) }

fun interface Authenticator {
    fun authenticate(username: String, password: CharArray): UserInfo?
}
