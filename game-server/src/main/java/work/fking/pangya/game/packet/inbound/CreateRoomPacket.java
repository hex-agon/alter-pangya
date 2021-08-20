package work.fking.pangya.game.packet.inbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.PacketFactory;
import work.fking.pangya.networking.protocol.ProtocolUtils;

public record CreateRoomPacket(
        int shotTime,
        int gameTime,
        int size,
        RoomType type,
        int holeCount,
        Course course,
        String name,
        String password
) implements InboundPacket {

    private static final int RANDOM_COURSE = 127;

    @PacketFactory
    public static InboundPacket decode(ByteBuf buffer) {
        buffer.skipBytes(1); // unknown
        var shotTime = buffer.readIntLE();
        var gameTime = buffer.readIntLE();
        var size = buffer.readByte();
        var typeId = buffer.readByte();
        var type = RoomType.forId(typeId);
        var holeCount = buffer.readByte();
        var course = Course.values()[buffer.readByte()];
        buffer.skipBytes(5); // unknown
        var name = ProtocolUtils.readPString(buffer);
        var password = ProtocolUtils.readPString(buffer);
        buffer.skipBytes(4); // unknown

        return new CreateRoomPacket(
                shotTime,
                gameTime,
                size,
                type,
                holeCount,
                course,
                name,
                password
        );
    }

    public enum RoomType {
        VERSUS,
        CHAT,
        TOURNAMENT,
        PANG_BATTLE,
        PRACTICE;

        public static RoomType forId(int id) {
            return switch (id) {
                case 0 -> VERSUS;
                case 2 -> CHAT;
                case 4 -> TOURNAMENT;
                case 10 -> PANG_BATTLE;
                case 19 -> PRACTICE;
                default -> null;
            };
        }
    }

    public enum Course {
        BLUE_LAGOON,
        BLUE_WATER,
        SEPIA_WIND,
        WIND_HILL,
        WIZ_WIZ,
        WEST_WIZ,
        BLUE_MOON,
        SILVIA_CANNON,
        ICE_CANNON,
        WHITE_WIZ,
        SHINING_SAND,
        PINK_WIND,
        DEEP_INFERNO,
        ICE_SPA,
        LOST_SEAWAY,
        EASTERN_VALLEY,
        ICE_INFERNO,
        WIZ_CITY,
        ABBOT_MINE
    }
}
