package work.fking.pangya.login;

import com.google.inject.Injector;
import work.fking.pangya.login.model.LoginSession;
import work.fking.pangya.login.model.PlayerAccount;
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

import javax.inject.Singleton;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class LoginServer {

    private static final int PORT = 10103;

    private final Map<Integer, LoginSession> sessions = new ConcurrentHashMap<>();

    public void register(LoginSession loginSession) {
        PlayerAccount playerAccount = loginSession.playerAccount();

        if (playerAccount == null) {
            throw new IllegalStateException("No account is currently associated with the given session");
        }
        sessions.put(playerAccount.id(), loginSession);
    }

    public void unregister(LoginSession loginSession) {
        PlayerAccount playerAccount = loginSession.playerAccount();

        if (playerAccount == null) {
            throw new IllegalStateException("No account is currently associated with the given session");
        }
        sessions.remove(playerAccount.id());
    }

    public LoginSession find(int userId) {
        return sessions.get(userId);
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

    public void start(Injector injector) throws IOException, InterruptedException {
        Protocol protocol = createProtocol();
        InboundPacketDispatcher packetDispatcher = createPacketDispatcher(injector);
        TailHandler tailHandler = new TailHandler(this);
        ServerChannelInitializer channelInitializer = ServerChannelInitializer.create(protocol, packetDispatcher, tailHandler);

        SimpleServer server = SimpleServer.builder()
                                          .channelInitializer(channelInitializer)
                                          .address(InetAddress.getByName("127.0.0.1"))
                                          .port(PORT)
                                          .build();

        server.start();
    }
}
