package work.fking.pangya.login.packet.handler;

import io.netty.buffer.ByteBuf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.fking.pangya.discovery.ServerType;
import work.fking.pangya.login.LoginServer;
import work.fking.pangya.login.Player;
import work.fking.pangya.login.net.ClientLoginPacketHandler;
import work.fking.pangya.login.net.LoginState;
import work.fking.pangya.login.packet.outbound.LoginReplies;
import work.fking.pangya.login.packet.outbound.ServerListReplies;
import work.fking.pangya.networking.protocol.ProtocolUtils;

public class LoginPacketHandler implements ClientLoginPacketHandler {

    private static final Logger LOGGER = LogManager.getLogger(LoginPacketHandler.class);

    @Override
    public void handle(LoginServer server, Player player, ByteBuf packet) {
        var username = ProtocolUtils.readPString(packet);
        var passwordMd5 = ProtocolUtils.readPStringCharArray(packet);

        var channel = player.channel();

        if (player.loginState() != LoginState.AUTHENTICATING) {
            LOGGER.warn("Unexpected login session state, got={}, expected=AUTHENTICATING", player.loginState());
            channel.disconnect();
            return;
        }
        var gameServers = server.discoveryClient().instances(ServerType.GAME);
        var socialServers = server.discoveryClient().instances(ServerType.SOCIAL);

        channel.write(LoginReplies.loginKey(player.loginKey()));
        channel.write(LoginReplies.chatMacros());
        channel.write(LoginReplies.success(player.id(), username, username));
        channel.write(ServerListReplies.gameServers(gameServers));
        channel.write(ServerListReplies.socialServers(socialServers));
        player.setLoginState(LoginState.AUTHENTICATED);
        player.setLoginState(LoginState.LOGGED_IN);
        channel.flush();
    }
}
