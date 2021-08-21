package work.fking.pangya.game;

import com.google.inject.Injector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.fking.pangya.common.server.ServerConfig;
import work.fking.pangya.game.packet.handler.CreateRoomPacketHandler;
import work.fking.pangya.game.packet.handler.EquipmentUpdatePacketHandler;
import work.fking.pangya.game.packet.handler.HandoverPacketHandler;
import work.fking.pangya.game.packet.handler.LockerInventoryRequestPacketHandler;
import work.fking.pangya.game.packet.handler.LoginBonusClaimPacketHandler;
import work.fking.pangya.game.packet.handler.LoginBonusStatusPacketHandler;
import work.fking.pangya.game.packet.handler.MyRoomOpenPacketHandler;
import work.fking.pangya.game.packet.handler.MyRoomOpenedPacketHandler;
import work.fking.pangya.game.packet.handler.SelectChannelPacketHandler;
import work.fking.pangya.game.packet.handler.Unknown156PacketHandler;
import work.fking.pangya.game.packet.handler.Unknown320PacketHandler;
import work.fking.pangya.game.packet.handler.UpdateChatMacrosPacketHandler;
import work.fking.pangya.game.packet.handler.UserProfileRequestPacketHandler;
import work.fking.pangya.game.packet.inbound.CreateRoomPacket;
import work.fking.pangya.game.packet.inbound.EquipmentUpdatePacket;
import work.fking.pangya.game.packet.inbound.HandoverPacket;
import work.fking.pangya.game.packet.inbound.LockerInventoryRequestPacket;
import work.fking.pangya.game.packet.inbound.LoginBonusClaimPacket;
import work.fking.pangya.game.packet.inbound.LoginBonusStatusPacket;
import work.fking.pangya.game.packet.inbound.MyRoomOpenPacket;
import work.fking.pangya.game.packet.inbound.MyRoomOpenedPacket;
import work.fking.pangya.game.packet.inbound.SelectChannelPacket;
import work.fking.pangya.game.packet.inbound.Unknown156Packet;
import work.fking.pangya.game.packet.inbound.Unknown320Packet;
import work.fking.pangya.game.packet.inbound.UpdateChatMacrosPacket;
import work.fking.pangya.game.packet.inbound.UserProfileRequestPacket;
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

    private Protocol createProtocol() {
        return Protocol.builder()
                       .inboundPacket(2, HandoverPacket.class)
                       .inboundPacket(4, SelectChannelPacket.class)
                       .inboundPacket(8, CreateRoomPacket.class)
                       .inboundPacket(0x20, EquipmentUpdatePacket.class)
                       .inboundPacket(0x2f, UserProfileRequestPacket.class)
                       .inboundPacket(0x69, UpdateChatMacrosPacket.class)
                       .inboundPacket(0x9C, Unknown156Packet.class)
                       .inboundPacket(0xB5, MyRoomOpenPacket.class)
                       .inboundPacket(0xB7, MyRoomOpenedPacket.class)
                       .inboundPacket(0xD3, LockerInventoryRequestPacket.class)
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
                                      .handler(EquipmentUpdatePacket.class, EquipmentUpdatePacketHandler.class)
                                      .handler(UserProfileRequestPacket.class, UserProfileRequestPacketHandler.class)
                                      .handler(UpdateChatMacrosPacket.class, UpdateChatMacrosPacketHandler.class)
                                      .handler(Unknown156Packet.class, Unknown156PacketHandler.class)
                                      .handler(MyRoomOpenPacket.class, MyRoomOpenPacketHandler.class)
                                      .handler(MyRoomOpenedPacket.class, MyRoomOpenedPacketHandler.class)
                                      .handler(LockerInventoryRequestPacket.class, LockerInventoryRequestPacketHandler.class)
                                      .handler(Unknown320Packet.class, Unknown320PacketHandler.class)
                                      .handler(LoginBonusStatusPacket.class, LoginBonusStatusPacketHandler.class)
                                      .handler(LoginBonusClaimPacket.class, LoginBonusClaimPacketHandler.class)
                                      .build();
    }

    public void start(Injector injector) throws IOException, InterruptedException {
        Protocol protocol = createProtocol();
        InboundPacketDispatcher packetDispatcher = createPacketDispatcher(injector);
        ServerChannelInitializer channelInitializer = ServerChannelInitializer.create(protocol, packetDispatcher);

        SimpleServer server = SimpleServer.builder()
                                          .channelInitializer(channelInitializer)
                                          .address(InetAddress.getByName(serverConfig.bindAddress()))
                                          .port(serverConfig.port())
                                          .build();
        server.start();
    }
}
