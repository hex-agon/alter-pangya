package work.fking.pangya.login;

import work.fking.pangya.common.server.ServerConfig;
import work.fking.pangya.login.model.LoginSession;
import work.fking.pangya.login.model.PlayerAccount;
import work.fking.pangya.networking.SimpleServer;
import work.fking.pangya.networking.protocol.InboundPacketDispatcher;
import work.fking.pangya.networking.protocol.Protocol;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class LoginServer {

    private final Map<Integer, LoginSession> sessions = new ConcurrentHashMap<>();

    private final ServerConfig serverConfig;

    @Inject
    public LoginServer(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

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
            return;
        }
        sessions.remove(playerAccount.id());
    }

    public LoginSession find(int userId) {
        return sessions.get(userId);
    }

    public void start(Protocol protocol, InboundPacketDispatcher packetDispatcher) throws IOException, InterruptedException {
        TailHandler tailHandler = new TailHandler(this);
        ServerChannelInitializer channelInitializer = ServerChannelInitializer.create(protocol, packetDispatcher, tailHandler);

        SimpleServer server = SimpleServer.builder()
                                          .channelInitializer(channelInitializer)
                                          .address(InetAddress.getByName(serverConfig.bindAddress()))
                                          .port(serverConfig.port())
                                          .build();

        server.start();
    }
}
