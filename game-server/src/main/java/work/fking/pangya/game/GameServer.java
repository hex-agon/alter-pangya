package work.fking.pangya.game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.fking.pangya.common.server.ServerConfig;
import work.fking.pangya.networking.SimpleServer;
import work.fking.pangya.networking.protocol.InboundPacketDispatcher;
import work.fking.pangya.networking.protocol.Protocol;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.InetAddress;

@Singleton
public class GameServer {

    private static final Logger LOGGER = LogManager.getLogger(GameServer.class);

    private final ServerConfig serverConfig;

    @Inject
    public GameServer(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public void start(Protocol protocol, InboundPacketDispatcher packetDispatcher) throws IOException, InterruptedException {
        ServerChannelInitializer channelInitializer = ServerChannelInitializer.create(protocol, packetDispatcher);

        SimpleServer server = SimpleServer.builder()
                                          .channelInitializer(channelInitializer)
                                          .address(InetAddress.getByName(serverConfig.bindAddress()))
                                          .port(serverConfig.port())
                                          .build();
        server.start();
    }
}
