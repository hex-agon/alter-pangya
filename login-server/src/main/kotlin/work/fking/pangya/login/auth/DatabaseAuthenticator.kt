package work.fking.pangya.login.auth

import com.password4j.Password
import work.fking.pangya.login.persistence.AccountRepository

class DatabaseAuthenticator(
    private val repository: AccountRepository
) : Authenticator {

    override fun authenticate(username: String, password: ByteArray): AuthResult {
        val userInfo = repository.loadUserInfo(username) ?: return InvalidCredentialsResult

        if (Password.check(password, userInfo.password).withBcrypt()) {
            return SuccessResult(userInfo)
        }
        return InvalidCredentialsResult
    }
}