package work.fking.pangya.game.packet.handler.room;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.common.Rand;
import work.fking.pangya.game.GameServer;
import work.fking.pangya.game.model.Course;
import work.fking.pangya.game.model.HoleMode;
import work.fking.pangya.game.model.RoomType;
import work.fking.pangya.game.net.ClientGamePacketHandler;
import work.fking.pangya.game.packet.outbound.RoomResponses;
import work.fking.pangya.game.player.Player;
import work.fking.pangya.networking.protocol.ProtocolUtils;

public class CreateRoomPacketHandler implements ClientGamePacketHandler {

    @Override
    public void handle(GameServer server, Player player, ByteBuf packet) {
        var unknown = packet.readByte();
        var shotTime = packet.readIntLE();
        var gameTime = packet.readIntLE();
        var maxPlayers = packet.readByte();
        var roomType = RoomType.forId(packet.readByte());
        var holeCount = packet.readByte();
        var course = Course.forId(packet.readByte());
        var holeMode = HoleMode.forId(packet.readByte());
        var unknown3 = packet.readIntLE();
        var name = ProtocolUtils.readPString(packet);
        var password = ProtocolUtils.readPString(packet);
        var artifactIffId = packet.readIntLE();

        var channel = player.channel();
        channel.write(RoomResponses.createSuccess(name, roomType, Rand.maxInclusive(127)));
        channel.write(RoomResponses.roomInfo(roomType, name, course, holeMode, holeCount, maxPlayers, shotTime, gameTime));

        channel.write(RoomResponses.roomInitialCensus(player));
        channel.write(RoomResponses.loungePkt196());
        channel.write(RoomResponses.loungePkt9e());
        channel.flush();
    }
}
