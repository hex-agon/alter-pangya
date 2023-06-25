package work.fking.pangya.game.packet.handler;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.game.GameServer;
import work.fking.pangya.game.player.Player;
import work.fking.pangya.game.net.ClientGamePacketHandler;
import work.fking.pangya.game.packet.outbound.LobbyResponses;

public class LeaveLobbyPacketHandler implements ClientGamePacketHandler {

    @Override
    public void handle(GameServer server, Player player, ByteBuf packet) {
        player.channel().writeAndFlush(LobbyResponses.ackLeave());
    }
}
