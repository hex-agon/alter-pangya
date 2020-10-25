package work.fking.pangya.game;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import lombok.extern.log4j.Log4j2;
import work.fking.pangya.game.packet.handler.HandoverPacketHandler;
import work.fking.pangya.game.packet.inbound.HandoverPacket;
import work.fking.pangya.networking.SimpleServer;
import work.fking.pangya.networking.protocol.InboundPacketDispatcher;
import work.fking.pangya.networking.protocol.Protocol;

import java.io.IOException;
import java.net.InetAddress;

@Log4j2
public class Bootstrap {

    private static final int PORT = 20202;

    private Protocol createProtocol() {
        return Protocol.builder()
                       .inboundPacket(2, HandoverPacket.class)
                       .build();
    }

    private InboundPacketDispatcher createPacketDispatcher(Injector injector) {
        return InboundPacketDispatcher.builder(injector::getInstance)
                                      .handler(HandoverPacket.class, HandoverPacketHandler.class)
                                      .build();
    }

    private void start() throws IOException, InterruptedException {
        Injector injector = Guice.createInjector(Stage.PRODUCTION);

        Protocol protocol = createProtocol();
        InboundPacketDispatcher packetDispatcher = createPacketDispatcher(injector);
        ServerChannelInitializer channelInitializer = ServerChannelInitializer.create(protocol, packetDispatcher);

        SimpleServer server = SimpleServer.builder()
                                          .channelInitializer(channelInitializer)
                                          .address(InetAddress.getByName("127.0.0.1"))
                                          .port(PORT)
                                          .build();

        server.start();
    }

    public static void main(String[] args) {
        LOGGER.info("Bootstrapping the game server...");
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.start();
        } catch (Exception e) {
            LOGGER.fatal("Failed to bootstrap the server", e);
        }
    }
}
