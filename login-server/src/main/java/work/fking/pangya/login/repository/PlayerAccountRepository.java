package work.fking.pangya.login.repository;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import work.fking.pangya.login.model.PlayerAccount;

public interface PlayerAccountRepository {

    @SqlQuery("SELECT id, username, nickname, password_hash, status, suspension_lift_timestamp FROM player_account WHERE username = :username")
    PlayerAccount findByUsername(@Bind("username") String username);

    @SqlQuery("SELECT exists(SELECT true FROM player_account WHERE nickname = :nickname)")
    boolean nicknameExists(@Bind("nickname") String nickname);

    @SqlUpdate("UPDATE player_account SET nickname = :nickname WHERE id = :accountId")
    boolean updateNickname(@Bind("accountId") int accountId, @Bind("nickname") String nickname);
}
