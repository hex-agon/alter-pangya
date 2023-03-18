package work.fking.pangya.login.packet.handler;

import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import work.fking.pangya.login.LoginServer;
import work.fking.pangya.login.Player;
import work.fking.pangya.login.net.ClientLoginPacketHandler;
import work.fking.pangya.login.packet.outbound.LoginReplies;

public class SelectServerPacketHandler implements ClientLoginPacketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SelectServerPacketHandler.class);

    @Override
    public void handle(LoginServer server, Player player, ByteBuf packet) {
        int serverId = packet.readShortLE();
        LOGGER.info("Player {} is being handed over to serverId={} with loginKey={} and sessionKey={}", player.id(), serverId, player.loginKey(), player.sessionKey());
        player.channel().writeAndFlush(LoginReplies.sessionKey(player.sessionKey()));
    }
}
