package work.fking.pangya.login.packet.handler;

import io.netty.buffer.ByteBuf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.fking.pangya.login.LoginServer;
import work.fking.pangya.login.Player;
import work.fking.pangya.login.net.ClientLoginPacketHandler;
import work.fking.pangya.login.net.LoginState;
import work.fking.pangya.networking.protocol.ProtocolUtils;

public class SetNicknamePacketHandler implements ClientLoginPacketHandler {

    private static final Logger LOGGER = LogManager.getLogger(SetNicknamePacketHandler.class);

    @Override
    public void handle(LoginServer server, Player player, ByteBuf packet) {
        var nickname = ProtocolUtils.readPString(packet);

        if (player.loginState() != LoginState.SELECTED_NICKNAME) {
            LOGGER.warn("Unexpected login session state, got={}, expected=SELECTED_NICKNAME", player.loginState());
            player.channel().disconnect();
        }
        player.setLoginState(LoginState.SELECTED_NICKNAME);
    }
}
