package work.fking.pangya.login;

import work.fking.pangya.common.server.ServerConfig;
import work.fking.pangya.common.server.ServerThread;
import work.fking.pangya.login.networking.ExceptionHandler;
import work.fking.pangya.login.networking.ServerChannelInitializer;
import work.fking.pangya.networking.SimpleServer;
import work.fking.pangya.networking.protocol.InboundPacketDispatcher;
import work.fking.pangya.networking.protocol.Protocol;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.InetAddress;

@Singleton
public class LoginServer {

    private final ServerConfig serverConfig;

    @Inject
    public LoginServer(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public void start(Protocol protocol, InboundPacketDispatcher packetDispatcher) throws IOException, InterruptedException {
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        ServerChannelInitializer channelInitializer = ServerChannelInitializer.create(protocol, packetDispatcher, exceptionHandler);

        SimpleServer server = SimpleServer.builder()
                                          .channelInitializer(channelInitializer)
                                          .address(InetAddress.getByName(serverConfig.bindAddress()))
                                          .port(serverConfig.port())
                                          .build();

        ServerThread.create(this::tick).start();

        server.start();
    }

    private void tick() {
    }
}
