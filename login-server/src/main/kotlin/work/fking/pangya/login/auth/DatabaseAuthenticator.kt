package work.fking.pangya.login.auth

import work.fking.pangya.login.persistence.AccountRepository

class DatabaseAuthenticator(val repository: AccountRepository) : Authenticator {

    override fun authenticate(username: String, password: CharArray): AuthResult {
        val userInfo = repository.loadUserInfo(username, password = ByteArray(0))

        return userInfo?.let { SuccessResult(it) } ?: InvalidCredentialsResult
    }
}