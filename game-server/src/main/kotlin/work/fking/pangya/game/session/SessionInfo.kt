package work.fking.pangya.game.session

@JvmRecord
data class SessionInfo(
    val sessionKey: String,
    val loginKey: String,
    val uid: Int,
    val username: String,
    val nickname: String,
    val characterIffId: Int?,
    val characterHairColor: Int?
)