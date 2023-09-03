package work.fking.pangya.login.session

import java.time.ZonedDateTime

data class SessionInfo(
    val key: String,
    val createdAt: ZonedDateTime = ZonedDateTime.now()
)
