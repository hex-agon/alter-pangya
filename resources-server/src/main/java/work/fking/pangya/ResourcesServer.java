package work.fking.pangya;

import lombok.extern.log4j.Log4j2;
import work.fking.pangya.networking.SimpleServer;

import java.net.InetAddress;

@Log4j2
public class ResourcesServer {

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
