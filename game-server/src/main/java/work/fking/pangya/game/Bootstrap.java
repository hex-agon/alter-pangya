package work.fking.pangya.game;

import com.google.inject.Guice;
import com.google.inject.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.fking.pangya.common.server.ServerConfigLoader;
import work.fking.pangya.discovery.DiscoveryClient;
import work.fking.pangya.discovery.HeartbeatPublisher;
import work.fking.pangya.discovery.ServerType;
import work.fking.pangya.game.module.DefaultModule;
import work.fking.pangya.game.module.RedisModule;

public class Bootstrap {

    private static final Logger LOGGER = LogManager.getLogger(Bootstrap.class);

    public static void main(String[] args) {
        LOGGER.info("Bootstrapping the game server server...");
        try {
            var serverConfig = ServerConfigLoader.load("config.toml");
            var injector = Guice.createInjector(Stage.PRODUCTION, RedisModule.create(), DefaultModule.create(serverConfig));
            var server = injector.getInstance(GameServer.class);

            var client = injector.getInstance(DiscoveryClient.class);
            var heartbeatPublisher = HeartbeatPublisher.create(client, ServerType.GAME, serverConfig, () -> 0);

            heartbeatPublisher.start();
            server.start(injector);
        } catch (Exception e) {
            LOGGER.fatal("Failed to bootstrap the server", e);
        }
    }
}
