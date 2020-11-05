package work.fking.pangya.login;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.fking.pangya.login.module.DatabaseModule;
import work.fking.pangya.login.module.SharedModule;
import work.fking.pangya.login.packet.handler.CheckNicknamePacketHandler;
import work.fking.pangya.login.packet.handler.LoginPacketHandler;
import work.fking.pangya.login.packet.handler.ReconnectPacketHandler;
import work.fking.pangya.login.packet.handler.SelectCharacterPacketHandler;
import work.fking.pangya.login.packet.handler.SelectServerPacketHandler;
import work.fking.pangya.login.packet.handler.SetNicknamePacketHandler;
import work.fking.pangya.login.packet.inbound.CheckNicknamePacket;
import work.fking.pangya.login.packet.inbound.GhostClientPacket;
import work.fking.pangya.login.packet.inbound.LoginRequestPacket;
import work.fking.pangya.login.packet.inbound.ReconnectPacket;
import work.fking.pangya.login.packet.inbound.SelectCharacterPacket;
import work.fking.pangya.login.packet.inbound.SelectServerPacket;
import work.fking.pangya.login.packet.inbound.SetNicknamePacket;
import work.fking.pangya.networking.SimpleServer;
import work.fking.pangya.networking.protocol.InboundPacketDispatcher;
import work.fking.pangya.networking.protocol.Protocol;

import java.io.IOException;
import java.net.InetAddress;

public class Bootstrap {

    private static final Logger LOGGER = LogManager.getLogger(Bootstrap.class);

    private static final int PORT = 10103;

    public static void main(String[] args) {
        LOGGER.info("Bootstrapping the login server...");
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.start();
        } catch (Exception e) {
            LOGGER.fatal("Failed to bootstrap the server", e);
        }
    }

    private Protocol createProtocol() {
        return Protocol.builder()
                       .inboundPacket(1, LoginRequestPacket.class)
                       .inboundPacket(3, SelectServerPacket.class)
                       .inboundPacket(4, GhostClientPacket.class)
                       .inboundPacket(6, SetNicknamePacket.class)
                       .inboundPacket(7, CheckNicknamePacket.class)
                       .inboundPacket(8, SelectCharacterPacket.class)
                       .inboundPacket(11, ReconnectPacket.class)
                       .build();
    }

    private InboundPacketDispatcher createPacketDispatcher(Injector injector) {
        return InboundPacketDispatcher.builder(injector::getInstance)
                                      .handler(LoginRequestPacket.class, LoginPacketHandler.class)
                                      .handler(SelectCharacterPacket.class, SelectCharacterPacketHandler.class)
                                      .handler(CheckNicknamePacket.class, CheckNicknamePacketHandler.class)
                                      .handler(SetNicknamePacket.class, SetNicknamePacketHandler.class)
                                      .handler(SelectServerPacket.class, SelectServerPacketHandler.class)
                                      .handler(ReconnectPacket.class, ReconnectPacketHandler.class)
                                      .build();
    }

    private void start() throws IOException, InterruptedException {
        Injector injector = Guice.createInjector(Stage.PRODUCTION, new SharedModule(), new DatabaseModule());

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
}
