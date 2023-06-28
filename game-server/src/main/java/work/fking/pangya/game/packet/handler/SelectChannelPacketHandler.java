package work.fking.pangya.game.packet.handler;

import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import work.fking.pangya.game.GameServer;
import work.fking.pangya.game.net.ClientPacketHandler;
import work.fking.pangya.game.packet.outbound.SelectChannelResultPacket;
import work.fking.pangya.game.player.Player;

public class SelectChannelPacketHandler implements ClientPacketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SelectChannelPacketHandler.class);

    @Override
    public void handle(GameServer server, Player player, ByteBuf packet) {
        var channelId = packet.readUnsignedByte();
        var channel = server.serverChannelById(channelId);

        if (channel == null) {
            LOGGER.warn("Player {} tried to join an unknown channel {}", player.nickname(), channelId);
            return;
        }
        LOGGER.info("Player {} joined channel {}", player.nickname(), channel.name());
        player.channel().writeAndFlush(new SelectChannelResultPacket());
    }
}
