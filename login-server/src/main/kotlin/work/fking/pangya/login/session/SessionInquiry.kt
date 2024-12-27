package work.fking.pangya.login.session

data class SessionInquiryResponse(val sessionId: String, val active: Boolean, val serverId: Int)

sealed interface FindSessionResult
data class Active(val serverId: Int) : FindSessionResult
object NotFound : FindSessionResult
object Timeout : FindSessionResult
data class Error(val exception: Throwable) : FindSessionResult
