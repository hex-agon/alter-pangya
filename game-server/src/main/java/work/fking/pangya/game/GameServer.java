package work.fking.pangya.game;

import io.netty.channel.Channel;
import work.fking.pangya.common.server.ServerConfig;
import work.fking.pangya.game.net.ClientGamePacketType;
import work.fking.pangya.game.net.ClientGameProtocol;
import work.fking.pangya.game.net.ServerChannelInitializer;
import work.fking.pangya.networking.SimpleServer;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicInteger;

public class GameServer {

    private final AtomicInteger playerIdSequence = new AtomicInteger();
    private final ServerConfig serverConfig;

    public GameServer(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public Player registerPlayer(Channel channel) {
        return new Player(playerIdSequence.incrementAndGet(), channel);
    }

    public void start() throws IOException, InterruptedException {
        var gameProtocol = ClientGameProtocol.create(ClientGamePacketType.values());
        var channelInitializer = ServerChannelInitializer.create(gameProtocol, this);

        var server = SimpleServer.builder()
                                 .channelInitializer(channelInitializer)
                                 .address(InetAddress.getByName(serverConfig.bindAddress()))
                                 .port(serverConfig.port())
                                 .build();
        server.start();
    }
}
