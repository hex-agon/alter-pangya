package work.fking.pangya.login;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.fking.pangya.login.module.DatabaseModule;
import work.fking.pangya.login.module.SharedModule;

public class Bootstrap {

    private static final Logger LOGGER = LogManager.getLogger(Bootstrap.class);

    public static void main(String[] args) {
        LOGGER.info("Bootstrapping the login server...");
        try {
            Injector injector = Guice.createInjector(Stage.PRODUCTION, new SharedModule(), new DatabaseModule());
            LoginServer loginServer = injector.getInstance(LoginServer.class);

            loginServer.start(injector);
        } catch (Exception e) {
            LOGGER.fatal("Failed to bootstrap the server", e);
        }
    }
}
