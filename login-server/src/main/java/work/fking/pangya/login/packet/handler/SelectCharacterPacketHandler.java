package work.fking.pangya.login.packet.handler;

import io.netty.buffer.ByteBuf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.fking.pangya.login.LoginServer;
import work.fking.pangya.login.Player;
import work.fking.pangya.login.net.ClientLoginPacketHandler;
import work.fking.pangya.login.net.LoginState;

public class SelectCharacterPacketHandler implements ClientLoginPacketHandler {

    private static final Logger LOGGER = LogManager.getLogger(SelectCharacterPacketHandler.class);

    @Override
    public void handle(LoginServer server, Player player, ByteBuf packet) {

        if (player.loginState() != LoginState.SELECTING_CHARACTER) {
            LOGGER.warn("Unexpected login session state, got={}, expected=SELECTING_CHARACTER", player.loginState());
            player.channel().disconnect();
        }
        player.setLoginState(LoginState.AUTHENTICATED);
    }
}
