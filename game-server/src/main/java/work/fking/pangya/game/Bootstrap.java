package work.fking.pangya.game;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.fking.pangya.common.server.ServerConfigLoader;
import work.fking.pangya.discovery.DiscoveryClient;
import work.fking.pangya.discovery.HeartbeatPublisher;
import work.fking.pangya.discovery.ServerType;

import java.util.List;

import static work.fking.pangya.game.ServerChannel.Restriction.BEGINNERS_AND_JUNIORS_ONLY;
import static work.fking.pangya.game.ServerChannel.Restriction.JUNIORS_AND_SENIORS_ONLY;
import static work.fking.pangya.game.ServerChannel.Restriction.ROOKIES_ONLY;

public class Bootstrap {

    private static final Logger LOGGER = LogManager.getLogger(Bootstrap.class);

    public static void main(String[] args) {
        LOGGER.info("Bootstrapping the game server server...");
        try {
            var serverConfig = ServerConfigLoader.load("config.toml");

            var redisClient = RedisClient.create(RedisURI.create(System.getenv("REDIS_URI")));
            var discoveryClient = DiscoveryClient.create(redisClient);
            var sessionClient = SessionClient.create(redisClient);

            var serverChannels = List.of(
                    new ServerChannel(1, "Rookies", 20, List.of(ROOKIES_ONLY)),
                    new ServerChannel(2, "Beginners & Juniors", 20, List.of(BEGINNERS_AND_JUNIORS_ONLY)),
                    new ServerChannel(3, "Juniors & Seniors", 20, List.of(JUNIORS_AND_SENIORS_ONLY)),
                    new ServerChannel(4, "Everyone", 20, List.of())

            );
            var server = new GameServer(serverConfig, sessionClient, serverChannels);

            var heartbeatPublisher = HeartbeatPublisher.create(discoveryClient, ServerType.GAME, serverConfig, server::playerCount);

            heartbeatPublisher.start();
            server.start();
        } catch (Exception e) {
            LOGGER.fatal("Failed to bootstrap the server", e);
        }
    }
}
