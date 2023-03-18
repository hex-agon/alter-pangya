package work.fking.pangya.game.packet.handler.room;

import io.netty.buffer.ByteBuf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.fking.pangya.common.Rand;
import work.fking.pangya.game.net.ClientGamePacketHandler;
import work.fking.pangya.game.GameServer;
import work.fking.pangya.game.Player;
import work.fking.pangya.game.packet.outbound.RoomResponses;
import work.fking.pangya.networking.protocol.ProtocolUtils;

public class CreateRoomPacketHandler implements ClientGamePacketHandler {

    private static final Logger LOGGER = LogManager.getLogger(CreateRoomPacketHandler.class);

    @Override
    public void handle(GameServer server, Player player, ByteBuf packet) {
        var unknown = packet.readByte();
        var shotTime = packet.readIntLE();
        var gameTime = packet.readIntLE();
        var size = packet.readByte();
        var typeId = packet.readByte();
        var type = RoomType.forId(typeId);
        var holeCount = packet.readByte();
        var course = Course.forId(packet.readByte());
        var unknown2 = packet.readByte();
        var unknown3 = packet.readIntLE();
        var name = ProtocolUtils.readPString(packet);
        var password = ProtocolUtils.readPString(packet);
        var unknown4 = packet.readIntLE();

        var channel = player.channel();
        channel.write(RoomResponses.createSuccess(name, 22));
        //        channel.write(RoomResponses.settings());
        channel.flush();
        //        channel.writeAndFlush(RoomResponses.roomInitialCensus());

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
