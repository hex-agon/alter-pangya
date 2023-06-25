package work.fking.pangya.game.packet.outbound;

import work.fking.pangya.game.player.Player;
import work.fking.pangya.game.model.CourseStatistics;
import work.fking.pangya.game.player.Character;
import work.fking.pangya.game.model.PlayerBasicInfo;
import work.fking.pangya.game.model.PlayerStatistic;
import work.fking.pangya.game.model.PlayerTrophies;
import work.fking.pangya.game.packet.handler.room.CreateRoomPacketHandler.Course;
import work.fking.pangya.networking.protocol.OutboundPacket;

public class UserStatisticsReplies {

    public static OutboundPacket username(int requestType, int userId) {
        return buffer -> {
            buffer.writeShortLE(0x157);
            buffer.writeByte(requestType);
            buffer.writeIntLE(userId);

            PlayerBasicInfo.mock().encode(buffer);

            buffer.writeZero(4); // unknown extra bytes
        };
    }

    public static OutboundPacket character(int userId) {
        return buffer -> {
            buffer.writeShortLE(0x15e);
            buffer.writeIntLE(userId);

            Character.mock().encode(buffer);
        };
    }

    public static OutboundPacket equipment(int requestType, Player player) {
        return buffer -> {
            buffer.writeShortLE(0x156);
            buffer.writeByte(requestType);
            buffer.writeIntLE(player.uid());

            player.equipment().encode(buffer);
        };
    }

    public static OutboundPacket userStatistic(int requestType, int userId) {
        return buffer -> {
            buffer.writeShortLE(0x158);
            buffer.writeByte(requestType);
            buffer.writeIntLE(userId);

            PlayerStatistic.mock().encode(buffer);
        };
    }

    public static OutboundPacket courseStatistic(int requestType, int userId) {
        return buffer -> {
            buffer.writeShortLE(0x15c);
            buffer.writeByte(requestType);
            buffer.writeIntLE(userId);

            // standard mode count
            buffer.writeIntLE(3);
            CourseStatistics.blank(Course.BLUE_LAGOON).serialize(buffer);
            CourseStatistics.blank(Course.SILVIA_CANNON).serialize(buffer);
            CourseStatistics.blank(Course.SEPIA_WIND).serialize(buffer);

            // assist mode count, seems unused
            buffer.writeIntLE(1);
            CourseStatistics.blank(Course.BLUE_LAGOON).serialize(buffer);
        };
    }

    public static OutboundPacket trophies(int requestType, int userId) {
        return buffer -> {
            buffer.writeShortLE(0x159);
            buffer.writeByte(requestType);
            buffer.writeIntLE(userId);

            PlayerTrophies.mock().encode(buffer);
        };
    }

    public static OutboundPacket ack(boolean valid, int requestType, int userId) {
        return buffer -> {
            buffer.writeShortLE(0x89);
            buffer.writeIntLE(valid ? 1 : 2); // 1 = valid, 2 = error
            buffer.writeByte(requestType);
            buffer.writeIntLE(userId);
        };
    }
}
