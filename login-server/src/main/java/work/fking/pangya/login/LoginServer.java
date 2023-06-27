package work.fking.pangya.login;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import work.fking.pangya.common.server.ServerConfig;
import work.fking.pangya.discovery.DiscoveryClient;
import work.fking.pangya.login.auth.Authenticator;
import work.fking.pangya.login.auth.UserInfo;
import work.fking.pangya.login.net.ServerChannelInitializer;
import work.fking.pangya.login.auth.SessionClient;
import work.fking.pangya.networking.SimpleServer;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginServer.class);

    private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
    private final DiscoveryClient discoveryClient;
    private final ServerConfig serverConfig;
    private final SessionClient sessionClient;
    private final Authenticator authenticator;

    public LoginServer(DiscoveryClient discoveryClient, ServerConfig serverConfig, SessionClient sessionClient, Authenticator authenticator) {
        this.discoveryClient = discoveryClient;
        this.serverConfig = serverConfig;
        this.sessionClient = sessionClient;

        this.authenticator = authenticator;
    }

    public DiscoveryClient discoveryClient() {
        return discoveryClient;
    }

    public SessionClient sessionRepository() {
        return sessionClient;
    }

    public Authenticator authenticator() {
        return authenticator;
    }

    public void submitTask(Runnable runnable) {
        executorService.execute(runnable);
    }

    public Player registerPlayer(Channel channel, UserInfo userInfo) {
        var player = new Player(channel, userInfo);
        channel.closeFuture().addListener(v -> onPlayerDisconnect(player));
        LOGGER.debug("{} logged in from {}", player.username(), channel.remoteAddress());
        return player;
    }

    private void onPlayerDisconnect(Player player) {
        LOGGER.debug("{} disconnected", player.username());
        sessionClient.unregisterSession(player);
    }

    public void start() throws IOException, InterruptedException {
        var channelInitializer = ServerChannelInitializer.create(this);

        SimpleServer server = SimpleServer.builder()
                                          .channelInitializer(channelInitializer)
                                          .address(InetAddress.getByName(serverConfig.bindAddress()))
                                          .port(serverConfig.port())
                                          .build();
        server.start();
    }
}
