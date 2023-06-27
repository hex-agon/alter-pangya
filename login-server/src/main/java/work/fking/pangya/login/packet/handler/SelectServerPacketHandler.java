package work.fking.pangya.login.packet.handler;

import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import work.fking.pangya.login.LoginServer;
import work.fking.pangya.login.Player;
import work.fking.pangya.login.net.ClientPacketHandler;
import work.fking.pangya.login.packet.outbound.LoginReplies;

import java.io.IOException;

public class SelectServerPacketHandler implements ClientPacketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SelectServerPacketHandler.class);

    @Override
    public void handle(LoginServer server, Player player, ByteBuf packet) {
        int serverId = packet.readShortLE();
        LOGGER.info("Player {} is being handed over to serverId={} with loginKey={} and sessionKey={}", player.uid(), serverId, player.loginKey(), player.sessionKey());

        var sessionRepository = server.sessionRepository();
        var channel = player.channel();

        try {
            sessionRepository.registerSession(player);
        } catch (IOException e) {
            LOGGER.warn("Failed to register player session", e);
            return;
        }
        channel.writeAndFlush(LoginReplies.sessionKey(player.sessionKey()));
    }
}
