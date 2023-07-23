package work.fking.pangya.login.auth

import java.util.concurrent.atomic.AtomicInteger

private val uidSequence = AtomicInteger(1)

val NOOP_AUTHENTICATOR: Authenticator = Authenticator { username, _ ->
    SuccessResult(
        UserInfo(
            uid = uidSequence.getAndIncrement(),
            username = username,
            needCharacterSelect = true
        )
    )
}

fun interface Authenticator {
    fun authenticate(username: String, password: CharArray): AuthResult
}

sealed interface AuthResult
data class SuccessResult(val userInfo: UserInfo) : AuthResult
data class ExceptionResult(val exception: Exception) : AuthResult
sealed class InvalidCredentialsResult : AuthResult
