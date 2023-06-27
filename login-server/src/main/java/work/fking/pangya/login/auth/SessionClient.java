package work.fking.pangya.login.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.sync.RedisCommands;
import work.fking.pangya.login.Player;

import java.io.IOException;

public class SessionClient {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final RedisCommands<String, String> redisCommands;

    private SessionClient(RedisCommands<String, String> redisCommands) {
        this.redisCommands = redisCommands;
    }

    public static SessionClient create(RedisClient redisClient) {
        return new SessionClient(redisClient.connect().sync());
    }

    public void registerSession(Player player) throws IOException {
        var userInfo = new SessionInfo(player.sessionKey(), player.uid(), player.username(), player.nickname());
        redisCommands.set(player.sessionKey(), OBJECT_MAPPER.writeValueAsString(userInfo));
    }

    public void unregisterSession(Player player) {
        redisCommands.expire(player.sessionKey(), 10);
    }

    private record SessionInfo(
            String sessionKey,
            int uid,
            String username,
            String nickname
    ) {

    }
}
