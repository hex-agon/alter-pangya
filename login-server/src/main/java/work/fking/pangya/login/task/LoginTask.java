package work.fking.pangya.login.task;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import work.fking.pangya.discovery.ServerType;
import work.fking.pangya.login.LoginServer;
import work.fking.pangya.login.auth.UserInfo;
import work.fking.pangya.login.net.ClientPacketDispatcher;
import work.fking.pangya.login.net.ClientPacketType;
import work.fking.pangya.login.net.ClientProtocol;
import work.fking.pangya.login.net.GameProtocolDecoder;
import work.fking.pangya.login.net.LoginState;
import work.fking.pangya.login.packet.outbound.LoginReplies;
import work.fking.pangya.login.packet.outbound.LoginReplies.Error;
import work.fking.pangya.login.packet.outbound.ServerListReplies;

public class LoginTask implements Runnable {

    private static final ClientProtocol PROTOCOL = ClientProtocol.create(ClientPacketType.values());
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginTask.class);

    private final LoginServer loginServer;
    private final Channel channel;
    private final int cryptKey;
    private final String username;
    private final char[] password;

    public LoginTask(LoginServer loginServer, Channel channel, int cryptKey, String username, char[] password) {
        this.loginServer = loginServer;
        this.channel = channel;
        this.cryptKey = cryptKey;
        this.username = username;
        this.password = password;
    }

    @Override
    public void run() {
        UserInfo userInfo;
        try {
            userInfo = loginServer.authenticator().authenticate(username, password);
        } catch (Exception e) {
            // TODO: improve this
            LOGGER.warn("Authentication failed", e);
            channel.writeAndFlush(LoginReplies.error(Error.INCORRECT_USERNAME_PASSWORD));
            return;
        }
        var player = loginServer.registerPlayer(channel, userInfo);

        var discoveryClient = loginServer.discoveryClient();
        var gameServers = discoveryClient.instances(ServerType.GAME);
        var socialServers = discoveryClient.instances(ServerType.SOCIAL);

        var pipeline = channel.pipeline();
        pipeline.remove("loginHandler");
        pipeline.addLast("decoder", GameProtocolDecoder.create(PROTOCOL, cryptKey));
        pipeline.addLast("packetDispatcher", ClientPacketDispatcher.create(loginServer, player, PROTOCOL.handlers()));

        channel.write(LoginReplies.loginKey(player.loginKey()));
        channel.write(LoginReplies.chatMacros());
        channel.write(LoginReplies.success(player.uid(), player.username(), player.nickname()));
        channel.write(ServerListReplies.gameServers(gameServers));
        channel.write(ServerListReplies.socialServers(socialServers));
        player.setLoginState(LoginState.LOGGED_IN);
        channel.flush();
    }
}
