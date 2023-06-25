package work.fking.pangya.game.packet.handler;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.game.GameServer;
import work.fking.pangya.game.player.Player;
import work.fking.pangya.game.net.ClientGamePacketHandler;
import work.fking.pangya.game.packet.outbound.UserStatisticsReplies;

public class UserProfileRequestPacketHandler implements ClientGamePacketHandler {

    @Override
    public void handle(GameServer server, Player player, ByteBuf packet) {
        var userId = packet.readIntLE();
        var type = packet.readByte();

        var channel = player.channel();
        if (type == 5) {
            channel.write(UserStatisticsReplies.username(type, userId));
            channel.write(UserStatisticsReplies.character(userId));
            channel.write(UserStatisticsReplies.equipment(type, player));
        }
        channel.write(UserStatisticsReplies.userStatistic(type, userId));
        channel.write(UserStatisticsReplies.courseStatistic(type, userId));
        channel.write(UserStatisticsReplies.trophies(type, userId));
        channel.write(UserStatisticsReplies.ack(true, type, userId));
        channel.flush();
    }
}
