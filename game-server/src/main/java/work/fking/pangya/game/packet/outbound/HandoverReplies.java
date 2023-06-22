package work.fking.pangya.game.packet.outbound;

import work.fking.pangya.game.model.CourseStatistics;
import work.fking.pangya.game.model.PangCaddie;
import work.fking.pangya.game.model.PangCharacter;
import work.fking.pangya.game.model.PlayerBasicInfo;
import work.fking.pangya.game.model.PlayerStatistic;
import work.fking.pangya.game.model.PlayerTrophies;
import work.fking.pangya.game.packet.handler.room.CreateRoomPacketHandler.Course;
import work.fking.pangya.networking.protocol.OutboundPacket;
import work.fking.pangya.networking.protocol.ProtocolUtils;

import java.time.LocalDateTime;

import static work.fking.pangya.networking.protocol.ProtocolUtils.writeFixedSizeString;

public class HandoverReplies {

    private static final int PACKET_ID = 0x44;

    private static final int TYPE_PROGRESS_BAR = 0xD2;
    private static final int TYPE_LOGIN_OK = 0xD3;

    private HandoverReplies() {
    }

    public static OutboundPacket ok() {
        return buffer -> {
            buffer.writeShortLE(PACKET_ID);
            buffer.writeShortLE(TYPE_LOGIN_OK);
        };
    }

    public static OutboundPacket error(HandoverResult result) {
        return buffer -> {
            buffer.writeShortLE(PACKET_ID);
            buffer.writeShortLE(result.code);
        };
    }

    public static OutboundPacket updateProgressBar(int value) {
        return buffer -> {
            buffer.writeShortLE(PACKET_ID);
            buffer.writeByte(TYPE_PROGRESS_BAR);
            buffer.writeByte(value);
        };
    }

    public static OutboundPacket handoverReply() {
        return buffer -> {
            buffer.writeShortLE(PACKET_ID);
            buffer.writeByte(0); // sub packet type

            // Server Info
            ProtocolUtils.writePString(buffer, "US852"); // server version
            ProtocolUtils.writePString(buffer, "hexserver_dev"); // server name

            // User info
            PlayerBasicInfo.mock().encode(buffer);

            // Player Statistics
            PlayerStatistic.mock().encode(buffer);

            // trophies
            PlayerTrophies.mock().encode(buffer);

            // Player equipment
            new EquipmentPacket().encode(buffer);

            // Season historical stats
            for (int i = 0; i < 12; i++) { // for each season...
                for (int j = 0; j < 21; j++) { // for each course...
                    CourseStatistics.blank(Course.BLUE_LAGOON).serialize(buffer);
                }
            }

            // Active Character
            PangCharacter.mock().encode(buffer);

            // Active Caddie
            PangCaddie.mock().encode(buffer);

            // Active Clubset
            buffer.writeIntLE(2000); // item unique id
            buffer.writeIntLE(268435511); // item iff id
            buffer.writeShortLE(5); // power slot
            buffer.writeShortLE(4); // control slot
            buffer.writeShortLE(3); // accuracy slot
            buffer.writeShortLE(2); // spin slot
            buffer.writeShortLE(1); // curve slot
            buffer.writeShortLE(1); // power upgrades?
            buffer.writeShortLE(2); // control upgrades?
            buffer.writeShortLE(3); // accuracy upgrades?
            buffer.writeShortLE(4); // spin upgrades?
            buffer.writeShortLE(5); // curve upgrades?

            // Active Mascot
            buffer.writeIntLE(0); // item unique id
            buffer.writeIntLE(0); // item iff id
            buffer.writeByte(0); // level
            buffer.writeIntLE(0); // experience
            writeFixedSizeString(buffer, "mascot1", 16);
            buffer.writeZero(33);

            // Server Time
            var now = LocalDateTime.now();
            buffer.writeShortLE(now.getYear());
            buffer.writeShortLE(now.getMonthValue());
            buffer.writeShortLE(now.getDayOfWeek().getValue());
            buffer.writeShortLE(now.getDayOfMonth());
            buffer.writeShortLE(now.getHour());
            buffer.writeShortLE(now.getMinute());
            buffer.writeShortLE(now.getSecond());
            buffer.writeShortLE(0); // milliseconds

            buffer.writeShortLE(0); // unknown

            // Papel shop info?
            buffer.writeShortLE(3);
            buffer.writeShortLE(2);
            buffer.writeShortLE(5);

            buffer.writeIntLE(0); // unknown
            buffer.writeLongLE(1 << 2); // disabled server features, 0x4 = disables mail

            buffer.writeIntLE(0); // unknown, ss = login count
            buffer.writeIntLE(0); // ss = server flags

            // guild info
            buffer.writeZero(277);

            // Remaining unknown data (321 bytes)
            // buffer.writeZero(321);

            // Based on acrisio's:
            // current server time (SYSTEMTIME 8 * 2 bytes)
            // short - unknown
            // papel shop info: short short short
            // int unknown
            // disabled server features: long
            // login count? int
            // GuildInfo, 277 bytes

        };
    }

    public enum HandoverResult {
        // 1, 9, 10, 12, 13 = causes the client to send select server packet & reconnect to the login server, probably used if the login key doesn't match
        // 3 = cannot connect to the login server
        // 5 = this id has been permanently blocked, contact support...
        // 7 = this id has been blocked, contact support...
        // 9 = reconnects too
        // 11 = Server version missmatch
        // 14, 15 = only certain allowed users may join this server.
        // 16, 17 = pangya is not available in your area
        // 19...31 = Account has been transferred
        // 210 = updates progress bar
        // 211 = login ok?
        RECONNECT_LOGIN_SERVER(1),
        CANNOT_CONNECT_LOGIN_SERVER(3),
        ID_PERMANENTLY_BLOCKED(5),
        ID_BLOCKED(7),
        SERVER_VERSION_MISSMATCH(11),
        NON_WHITELISTED_USER(14),
        GEO_BLOCKED(16),
        ACCOUNT_TRANSFERRED(19),
        ;

        private final int code;

        HandoverResult(int code) {
            this.code = code;
        }
    }
}
