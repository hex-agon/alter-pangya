package work.fking.pangya.login.auth

@JvmRecord
data class UserInfo(
    val uid: Int,
    val password: ByteArray,
    val username: String,
    val nickname: String? = null,
    val hasBaseCharacter: Boolean = false
)
