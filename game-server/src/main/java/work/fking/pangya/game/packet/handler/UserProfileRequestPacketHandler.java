package work.fking.pangya.game.packet.handler;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.game.net.ClientGamePacketHandler;
import work.fking.pangya.game.GameServer;
import work.fking.pangya.game.Player;

public class UserProfileRequestPacketHandler implements ClientGamePacketHandler {


    @Override
    public void handle(GameServer server, Player player, ByteBuf packet) {
        var userId = packet.readIntLE();
        var type = packet.readByte();
    }
}
