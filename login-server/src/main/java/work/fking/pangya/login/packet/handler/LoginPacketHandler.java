package work.fking.pangya.login.packet.handler;

import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.fking.pangya.discovery.DiscoveryClient;
import work.fking.pangya.discovery.ServerType;
import work.fking.pangya.login.networking.ConnectionState;
import work.fking.pangya.login.packet.inbound.LoginRequestPacket;
import work.fking.pangya.login.packet.outbound.LoginReplies;
import work.fking.pangya.login.packet.outbound.ServerListReplies;
import work.fking.pangya.networking.protocol.InboundPacketHandler;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LoginPacketHandler implements InboundPacketHandler<LoginRequestPacket> {

    private static final Logger LOGGER = LogManager.getLogger(LoginPacketHandler.class);

    private final DiscoveryClient discoveryClient;

    @Inject
    public LoginPacketHandler(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Override
    public void handle(Channel channel, LoginRequestPacket packet) {
        ConnectionState state = channel.attr(ConnectionState.KEY).get();

        if (state != ConnectionState.AUTHENTICATING) {
            LOGGER.warn("Unexpected login session state, got={}, expected=AUTHENTICATING", state);
            channel.disconnect();
            return;
        }
        var gameServers = discoveryClient.instances(ServerType.GAME);
        var socialServers = discoveryClient.instances(ServerType.SOCIAL);

        channel.write(LoginReplies.loginKey("loginKey"));
        channel.write(LoginReplies.chatMacros());
        channel.write(LoginReplies.success(1, "hexagon", "Hexagon"));
        channel.write(ServerListReplies.gameServers(gameServers));
        channel.write(ServerListReplies.socialServers(socialServers));
        channel.attr(ConnectionState.KEY).set(ConnectionState.LOGGED_IN);
        channel.flush();
    }
}
