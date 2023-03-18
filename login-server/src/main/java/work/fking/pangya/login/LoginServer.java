package work.fking.pangya.login;

import io.netty.channel.Channel;
import work.fking.pangya.common.server.ServerConfig;
import work.fking.pangya.discovery.DiscoveryClient;
import work.fking.pangya.login.net.ClientLoginPacketType;
import work.fking.pangya.login.net.ClientLoginProtocol;
import work.fking.pangya.login.net.ServerChannelInitializer;
import work.fking.pangya.networking.SimpleServer;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicInteger;

public class LoginServer {

    private final AtomicInteger playerIdSequence = new AtomicInteger();
    private final DiscoveryClient discoveryClient;
    private final ServerConfig serverConfig;

    public LoginServer(DiscoveryClient discoveryClient, ServerConfig serverConfig) {
        this.discoveryClient = discoveryClient;
        this.serverConfig = serverConfig;
    }

    public DiscoveryClient discoveryClient() {
        return discoveryClient;
    }

    public Player registerPlayer(Channel channel) {
        return new Player(playerIdSequence.incrementAndGet(), channel);
    }

    public void start() throws IOException, InterruptedException {
        var loginProtocol = ClientLoginProtocol.create(ClientLoginPacketType.values());
        var channelInitializer = ServerChannelInitializer.create(loginProtocol, this);

        SimpleServer server = SimpleServer.builder()
                                          .channelInitializer(channelInitializer)
                                          .address(InetAddress.getByName(serverConfig.bindAddress()))
                                          .port(serverConfig.port())
                                          .build();
        server.start();
    }
}
