package work.fking.pangya.game.packet.handler.room;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.game.net.ClientGamePacketHandler;
import work.fking.pangya.game.GameServer;
import work.fking.pangya.game.player.Player;
import work.fking.pangya.game.packet.outbound.RoomResponses;

public class LeaveRoomPacketHandler implements ClientGamePacketHandler {

    @Override
    public void handle(GameServer server, Player player, ByteBuf packet) {
        packet.readByte(); // unknown (either 0 or 1)
        var roomId = packet.readShortLE();
        packet.readIntLE(); // unknown
        packet.readIntLE(); // unknown
        packet.readIntLE(); // unknown
        packet.readIntLE(); // unknown
        player.channel().writeAndFlush(RoomResponses.leaveSuccess());
    }
}
