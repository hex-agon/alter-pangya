package work.fking.pangya.game;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.fking.pangya.game.packet.handler.CreateRoomPacketHandler;
import work.fking.pangya.game.packet.handler.HandoverPacketHandler;
import work.fking.pangya.game.packet.handler.LoginBonusClaimPacketHandler;
import work.fking.pangya.game.packet.handler.LoginBonusStatusPacketHandler;
import work.fking.pangya.game.packet.handler.SelectChannelPacketHandler;
import work.fking.pangya.game.packet.handler.Unknown156PacketHandler;
import work.fking.pangya.game.packet.handler.Unknown320PacketHandler;
import work.fking.pangya.game.packet.handler.UpdateChatMacrosPacketHandler;
import work.fking.pangya.game.packet.handler.UserProfileRequestPacketHandler;
import work.fking.pangya.game.packet.inbound.CreateRoomPacket;
import work.fking.pangya.game.packet.inbound.HandoverPacket;
import work.fking.pangya.game.packet.inbound.LoginBonusClaimPacket;
import work.fking.pangya.game.packet.inbound.LoginBonusStatusPacket;
import work.fking.pangya.game.packet.inbound.SelectChannelPacket;
import work.fking.pangya.game.packet.inbound.Unknown156Packet;
import work.fking.pangya.game.packet.inbound.Unknown320Packet;
import work.fking.pangya.game.packet.inbound.UpdateChatMacrosPacket;
import work.fking.pangya.game.packet.inbound.UserProfileRequestPacket;
import work.fking.pangya.networking.SimpleServer;
import work.fking.pangya.networking.protocol.InboundPacketDispatcher;
import work.fking.pangya.networking.protocol.Protocol;

import java.io.IOException;
import java.net.InetAddress;

public class Bootstrap {

    private static final Logger LOGGER = LogManager.getLogger(Bootstrap.class);

    private static final int PORT = 20202;

    public static void main(String[] args) {
        LOGGER.info("Bootstrapping the game server...");
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.start();
        } catch (Exception e) {
            LOGGER.fatal("Failed to bootstrap the server", e);
        }
    }

    private Protocol createProtocol() {
        return Protocol.builder()
                       .inboundPacket(2, HandoverPacket.class)
                       .inboundPacket(4, SelectChannelPacket.class)
                       .inboundPacket(8, CreateRoomPacket.class)
                       .inboundPacket(0x2f, UserProfileRequestPacket.class)
                       .inboundPacket(0x69, UpdateChatMacrosPacket.class)
                       .inboundPacket(0x9C, Unknown156Packet.class)
                       .inboundPacket(0x140, Unknown320Packet.class)
                       .inboundPacket(0x16E, LoginBonusStatusPacket.class)
                       .inboundPacket(0x16F, LoginBonusClaimPacket.class)
                       .build();
    }

    private InboundPacketDispatcher createPacketDispatcher(Injector injector) {
        return InboundPacketDispatcher.builder(injector::getInstance)
                                      .handler(HandoverPacket.class, HandoverPacketHandler.class)
                                      .handler(SelectChannelPacket.class, SelectChannelPacketHandler.class)
                                      .handler(CreateRoomPacket.class, CreateRoomPacketHandler.class)
                                      .handler(UserProfileRequestPacket.class, UserProfileRequestPacketHandler.class)
                                      .handler(UpdateChatMacrosPacket.class, UpdateChatMacrosPacketHandler.class)
                                      .handler(Unknown156Packet.class, Unknown156PacketHandler.class)
                                      .handler(Unknown320Packet.class, Unknown320PacketHandler.class)
                                      .handler(LoginBonusStatusPacket.class, LoginBonusStatusPacketHandler.class)
                                      .handler(LoginBonusClaimPacket.class, LoginBonusClaimPacketHandler.class)
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
}
