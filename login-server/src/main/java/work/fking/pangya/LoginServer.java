package work.fking.pangya;

import lombok.extern.log4j.Log4j2;
import work.fking.pangya.networking.SimpleServer;
import work.fking.pangya.networking.protocol.Protocol;
import work.fking.pangya.packet.inbound.LoginRequestPacket;

import java.net.InetAddress;

@Log4j2
public class LoginServer {

    private static final int PORT = 10103;

    public static void main(String[] args) {
        LOGGER.info("Bootstrapping the login server...");
        try {
            Protocol protocol = Protocol.create()
                                        .registerInboundPacket(1, LoginRequestPacket.class);

            SimpleServer server = SimpleServer.builder()
                                              .channelInitializer(LoginServerChannelInitializer.create(protocol))
                                              .address(InetAddress.getByName("127.0.0.1"))
                                              .port(PORT)
                                              .build();

            server.start();
        } catch (Exception e) {
            LOGGER.fatal("Failed to bootstrap the server", e);
        }
    }
}
