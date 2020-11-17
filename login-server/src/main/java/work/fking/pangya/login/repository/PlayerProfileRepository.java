package work.fking.pangya.login.repository;

import org.jdbi.v3.json.Json;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import work.fking.pangya.login.model.BasicPlayerProfile;

public interface PlayerProfileRepository {

    @SqlUpdate("INSERT INTO player_profile VALUES (:accountId, :profile)")
    boolean createProfile(@Bind("accountId") int accountId, @Bind("profile") @Json BasicPlayerProfile profile);

    @SqlQuery("SELECT exists(SELECT profile_json->'activeCharacter' FROM player_profile WHERE player_account_id = :accountId)")
    boolean hasActiveCharacter(@Bind("accountId") int accountId);
}
