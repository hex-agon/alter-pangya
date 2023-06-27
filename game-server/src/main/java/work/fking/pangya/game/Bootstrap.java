package work.fking.pangya.game;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.fking.pangya.common.server.ServerConfigLoader;
import work.fking.pangya.discovery.DiscoveryClient;
import work.fking.pangya.discovery.HeartbeatPublisher;
import work.fking.pangya.discovery.ServerType;

public class Bootstrap {

    private static final Logger LOGGER = LogManager.getLogger(Bootstrap.class);

    public static void main(String[] args) {
        LOGGER.info("Bootstrapping the game server server...");
        try {
            var serverConfig = ServerConfigLoader.load("config.toml");

            var redisClient = RedisClient.create(RedisURI.create(System.getenv("REDIS_URI")));
            var discoveryClient = DiscoveryClient.create(redisClient);
            var sessionClient = SessionClient.create(redisClient);

            var server = new GameServer(serverConfig, sessionClient);

            var heartbeatPublisher = HeartbeatPublisher.create(discoveryClient, ServerType.GAME, serverConfig, () -> 0);

            heartbeatPublisher.start();
            server.start();
        } catch (Exception e) {
            LOGGER.fatal("Failed to bootstrap the server", e);
        }
    }
}
