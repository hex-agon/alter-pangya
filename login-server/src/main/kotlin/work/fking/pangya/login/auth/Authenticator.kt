package work.fking.pangya.login.auth


val NOOP_AUTHENTICATOR: Authenticator = Authenticator { _, _ -> InvalidCredentialsResult }

fun interface Authenticator {
    fun authenticate(username: String, password: CharArray): AuthResult
}

sealed interface AuthResult
data class SuccessResult(val userInfo: UserInfo) : AuthResult
data class ExceptionResult(val exception: Exception) : AuthResult
data object InvalidCredentialsResult : AuthResult
