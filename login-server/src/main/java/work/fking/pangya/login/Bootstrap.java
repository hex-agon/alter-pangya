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
import work.fking.pangya.networking.protocol.ProtocolScanner;

public class Bootstrap {

    private static final Logger LOGGER = LogManager.getLogger(Bootstrap.class);

    public static void main(String[] args) {
        LOGGER.info("Bootstrapping the login server...");
        try {
            LOGGER.debug("Loading server config...");
            var serverConfig = ServerConfigLoader.load("config.toml");
            Injector injector = Guice.createInjector(Stage.PRODUCTION, RedisModule.create(), DefaultModule.create(serverConfig), DatabaseModule.create());

            var scanResult = ProtocolScanner.scan(injector::getInstance);
            var loginServer = injector.getInstance(LoginServer.class);

            LOGGER.debug("Initializing service discovery...");
            var client = injector.getInstance(DiscoveryClient.class);
            var heartbeatPublisher = HeartbeatPublisher.create(client, ServerType.LOGIN, serverConfig, () -> 0);

            heartbeatPublisher.start();

            LOGGER.debug("Starting the login server...");
            loginServer.start(scanResult.protocol(), scanResult.packetDispatcher());
        } catch (Exception e) {
            LOGGER.fatal("Failed to bootstrap the server", e);
        }
    }
}
