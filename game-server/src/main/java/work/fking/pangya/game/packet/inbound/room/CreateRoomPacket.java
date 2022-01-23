package work.fking.pangya.game.packet.inbound.room;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.common.Rand;
import work.fking.pangya.game.packet.handler.room.CreateRoomPacketHandler;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.Packet;
import work.fking.pangya.networking.protocol.PacketFactory;
import work.fking.pangya.networking.protocol.ProtocolUtils;

@Packet(id = 0x8, handledBy = CreateRoomPacketHandler.class)
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
        var unknown = buffer.readByte();
        var shotTime = buffer.readIntLE();
        var gameTime = buffer.readIntLE();
        var size = buffer.readByte();
        var typeId = buffer.readByte();
        var type = RoomType.forId(typeId);
        var holeCount = buffer.readByte();
        var course = Course.forId(buffer.readByte());
        var unknown2 = buffer.readByte();
        var unknown3 = buffer.readIntLE();
        var name = ProtocolUtils.readPString(buffer);
        var password = ProtocolUtils.readPString(buffer);
        var unknown4 = buffer.readIntLE();

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
        VERSUS(0),
        CHAT(2),
        TOURNAMENT(4),
        PANG_BATTLE(10),
        PRACTICE(19);

        private final int id;

        RoomType(int id) {
            this.id = id;
        }

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

        public int id() {
            return id;
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
        ABBOT_MINE;

        public static Course forId(int id) {
            var values = values();

            if (id < values.length) {
                return values[id];
            } else { // anything else or random (id == 127)
                return values[Rand.max(values.length)];
            }
        }
    }
}
