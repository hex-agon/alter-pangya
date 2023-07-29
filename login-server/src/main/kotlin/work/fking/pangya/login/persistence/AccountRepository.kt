package work.fking.pangya.login.persistence

import work.fking.pangya.login.auth.UserInfo
import javax.sql.DataSource

class AccountRepository(private val dataSource: DataSource) {

    private val query = """
        SELECT a.uid AS uid,
               a.username AS username,
               a.nickname AS nickname,
               EXISTS(SELECT 1 FROM player_character pc WHERE pc.account_uid = a.uid) AS hasbasecharacter
        FROM account a
        WHERE username = ?
    """.trimIndent()

    fun loadUserInfo(username: String, password: ByteArray): UserInfo? {
        dataSource.connection.use { conn ->
            conn.prepareStatement(query).use { stmt ->
                stmt.setString(1, username)
                stmt.executeQuery().use { rs ->
                    if (rs.next()) {
                        return UserInfo(
                            uid = rs.getInt("uid"),
                            username = rs.getString("username"),
                            nickname = rs.getString("nickname"),
                            hasBaseCharacter = rs.getBoolean("hasbasecharacter")
                        )
                    }
                }
            }
        }
        return null
    }
}