package work.fking.pangya.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.sync.RedisCommands;

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

    public SessionInfo loadSession(String sessionKey) throws IOException {
        var json = redisCommands.get(sessionKey);
        if (json == null) {
            return null;
        }
        return OBJECT_MAPPER.readValue(json, SessionInfo.class);
    }

    public record SessionInfo(
            String sessionKey,
            int uid,
            String username,
            String nickname
    ) {

    }
}
