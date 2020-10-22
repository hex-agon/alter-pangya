package work.fking.pangya;

import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.extern.log4j.Log4j2;
import work.fking.pangya.networking.SimpleServer;
import work.fking.pangya.networking.protocol.InboundPacketDispatcher;
import work.fking.pangya.networking.protocol.Protocol;
import work.fking.pangya.packet.handler.LoginPacketHandler;
import work.fking.pangya.packet.inbound.CheckNicknamePacket;
import work.fking.pangya.packet.inbound.GhostClientPacket;
import work.fking.pangya.packet.inbound.LoginRequestPacket;
import work.fking.pangya.packet.inbound.ReconnectPacket;
import work.fking.pangya.packet.inbound.SelectCharacterPacket;
import work.fking.pangya.packet.inbound.SelectServerPacket;
import work.fking.pangya.packet.inbound.SetNicknamePacket;

import java.net.InetAddress;

@Log4j2
public class LoginServer {

    private static final int PORT = 10103;

    public static void main(String[] args) {
        LOGGER.info("Bootstrapping the login server...");
        try {
            Protocol protocol = Protocol.create()
                                        .registerInboundPacket(1, LoginRequestPacket.class)
                                        .registerInboundPacket(3, SelectServerPacket.class)
                                        .registerInboundPacket(4, GhostClientPacket.class)
                                        .registerInboundPacket(6, SetNicknamePacket.class)
                                        .registerInboundPacket(7, CheckNicknamePacket.class)
                                        .registerInboundPacket(8, SelectCharacterPacket.class)
                                        .registerInboundPacket(11, ReconnectPacket.class);

            Injector injector = Guice.createInjector();

            InboundPacketDispatcher packetDispatcher = InboundPacketDispatcher.create(injector::getInstance)
                                                                              .registerHandler(LoginRequestPacket.class, LoginPacketHandler.class);

            LoginServerChannelInitializer channelInitializer = LoginServerChannelInitializer.create(protocol, packetDispatcher);

            SimpleServer server = SimpleServer.builder()
                                              .channelInitializer(channelInitializer)
                                              .address(InetAddress.getByName("127.0.0.1"))
                                              .port(PORT)
                                              .build();

            server.start();
        } catch (Exception e) {
            LOGGER.fatal("Failed to bootstrap the server", e);
        }
    }
}
