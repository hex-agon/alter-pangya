package work.fking.pangya.login.packet.handler;

import io.netty.buffer.ByteBuf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.fking.pangya.login.LoginServer;
import work.fking.pangya.login.Player;
import work.fking.pangya.login.net.ClientPacketHandler;
import work.fking.pangya.login.net.LoginState;
import work.fking.pangya.login.packet.outbound.CheckNicknameReplies;
import work.fking.pangya.networking.protocol.ProtocolUtils;

public class CheckNicknamePacketHandler implements ClientPacketHandler {

    private static final Logger LOGGER = LogManager.getLogger(CheckNicknamePacketHandler.class);

    @Override
    public void handle(LoginServer server, Player player, ByteBuf packet) {
        var nickname = ProtocolUtils.readPString(packet);
        var channel = player.channel();

        if (player.loginState() != LoginState.SELECTING_NICKNAME && player.loginState() != LoginState.SELECTED_NICKNAME) {
            LOGGER.warn("Unexpected login session state, got={}, expected=SELECTING_NICKNAME or SELECTED_NICKNAME", player.loginState());
            channel.disconnect();
            return;
        }
        channel.writeAndFlush(CheckNicknameReplies.available(nickname));
    }
}
