package work.fking.pangya.resources;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.fking.pangya.networking.SimpleServer;

import java.net.InetAddress;

public class ResourcesServer {

    private static final Logger LOGGER = LogManager.getLogger(ResourcesServer.class);

    public static final int MAX_CONTENT_LENGTH = 65536;
    private static final int PORT = 50009;

    public static void main(String[] args) {
        LOGGER.info("Bootstrapping the resources server...");
        try {
            SimpleServer server = SimpleServer.builder()
                                              .channelInitializer(new HttpServerChannelInitializer())
                                              .address(InetAddress.getByName("127.0.0.1"))
                                              .port(PORT)
                                              .build();

            server.start();
        } catch (Exception e) {
            LOGGER.fatal("Failed to bootstrap the server", e);
        }
    }
}
