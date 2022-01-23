package work.fking.pangya.game;

import com.google.inject.Guice;
import com.google.inject.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.fking.pangya.common.server.ServerConfigLoader;
import work.fking.pangya.discovery.DiscoveryClient;
import work.fking.pangya.discovery.HeartbeatPublisher;
import work.fking.pangya.discovery.ServerType;
import work.fking.pangya.game.module.DefaultModule;
import work.fking.pangya.game.module.RedisModule;
import work.fking.pangya.game.packet.inbound.EquipmentUpdatePacket;
import work.fking.pangya.game.packet.inbound.HandoverPacket;
import work.fking.pangya.game.packet.inbound.LockerInventoryRequestPacket;
import work.fking.pangya.game.packet.inbound.LoginBonusClaimPacket;
import work.fking.pangya.game.packet.inbound.LoginBonusStatusPacket;
import work.fking.pangya.game.packet.inbound.MyRoomOpenPacket;
import work.fking.pangya.game.packet.inbound.MyRoomOpenedPacket;
import work.fking.pangya.game.packet.inbound.RareShopOpenPacket;
import work.fking.pangya.game.packet.inbound.SelectChannelPacket;
import work.fking.pangya.game.packet.inbound.Unknown156Packet;
import work.fking.pangya.game.packet.inbound.Unknown320Packet;
import work.fking.pangya.game.packet.inbound.UpdateChatMacrosPacket;
import work.fking.pangya.game.packet.inbound.UserProfileRequestPacket;
import work.fking.pangya.game.packet.inbound.room.CreateRoomPacket;
import work.fking.pangya.game.packet.inbound.room.LeaveRoomPacket;
import work.fking.pangya.networking.protocol.InboundPacketDispatcher;
import work.fking.pangya.networking.protocol.Protocol;

public class Bootstrap {

    private static final Logger LOGGER = LogManager.getLogger(Bootstrap.class);

    public static void main(String[] args) {
        LOGGER.info("Bootstrapping the game server server...");
        try {
            var serverConfig = ServerConfigLoader.load("config.toml");
            var injector = Guice.createInjector(Stage.PRODUCTION, RedisModule.create(), DefaultModule.create(serverConfig));
            var server = injector.getInstance(GameServer.class);

            var protocol = Protocol.builder()
                                   .register(CreateRoomPacket.class)
                                   .register(LeaveRoomPacket.class)
                                   .register(EquipmentUpdatePacket.class)
                                   .register(HandoverPacket.class)
                                   .register(LockerInventoryRequestPacket.class)
                                   .register(LoginBonusClaimPacket.class)
                                   .register(LoginBonusStatusPacket.class)
                                   .register(MyRoomOpenedPacket.class)
                                   .register(MyRoomOpenPacket.class)
                                   .register(RareShopOpenPacket.class)
                                   .register(SelectChannelPacket.class)
                                   .register(Unknown156Packet.class)
                                   .register(Unknown320Packet.class)
                                   .register(UpdateChatMacrosPacket.class)
                                   .register(UserProfileRequestPacket.class)
                                   .build();
            var packetDispatcher = InboundPacketDispatcher.builder(injector::getInstance)
                                                          .register(CreateRoomPacket.class)
                                                          .register(LeaveRoomPacket.class)
                                                          .register(EquipmentUpdatePacket.class)
                                                          .register(HandoverPacket.class)
                                                          .register(LockerInventoryRequestPacket.class)
                                                          .register(LoginBonusClaimPacket.class)
                                                          .register(LoginBonusStatusPacket.class)
                                                          .register(MyRoomOpenedPacket.class)
                                                          .register(MyRoomOpenPacket.class)
                                                          .register(RareShopOpenPacket.class)
                                                          .register(SelectChannelPacket.class)
                                                          .register(Unknown156Packet.class)
                                                          .register(Unknown320Packet.class)
                                                          .register(UpdateChatMacrosPacket.class)
                                                          .register(UserProfileRequestPacket.class)
                                                          .build();

            var client = injector.getInstance(DiscoveryClient.class);
            var heartbeatPublisher = HeartbeatPublisher.create(client, ServerType.GAME, serverConfig, () -> 0);

            heartbeatPublisher.start();
            server.start(protocol, packetDispatcher);
        } catch (Exception e) {
            LOGGER.fatal("Failed to bootstrap the server", e);
        }
    }
}
