package work.fking.pangya.login.session

data class HandoverInfo(
    val sessionKey: String,
    val loginKey: String,
    val uid: Int,
    val username: String,
    val nickname: String,
    val characterIffId: Int?,
    val characterHairColor: Int?
)