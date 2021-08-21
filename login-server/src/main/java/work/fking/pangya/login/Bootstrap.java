package work.fking.pangya.login;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.fking.pangya.common.server.ServerConfigLoader;
import work.fking.pangya.discovery.DiscoveryClient;
import work.fking.pangya.discovery.HeartbeatPublisher;
import work.fking.pangya.discovery.ServerType;
import work.fking.pangya.login.module.DatabaseModule;
import work.fking.pangya.login.module.DefaultModule;
import work.fking.pangya.login.module.RedisModule;

public class Bootstrap {

    private static final Logger LOGGER = LogManager.getLogger(Bootstrap.class);

    public static void main(String[] args) {
        LOGGER.info("Bootstrapping the login server...");
        try {
            var serverConfig = ServerConfigLoader.load("config.toml");
            Injector injector = Guice.createInjector(Stage.PRODUCTION, RedisModule.create(), DefaultModule.create(serverConfig), new DatabaseModule());
            LoginServer loginServer = injector.getInstance(LoginServer.class);

            var client = injector.getInstance(DiscoveryClient.class);
            var heartbeatPublisher = HeartbeatPublisher.create(client, ServerType.LOGIN, serverConfig, () -> 0);

            heartbeatPublisher.start();

            loginServer.start(injector);
        } catch (Exception e) {
            LOGGER.fatal("Failed to bootstrap the server", e);
        }
    }
}
