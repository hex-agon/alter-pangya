package work.fking.pangya.game.packet.outbound;

import work.fking.pangya.game.model.PangCaddie;
import work.fking.pangya.game.model.PangCharacter;
import work.fking.pangya.networking.protocol.OutboundPacket;
import work.fking.pangya.networking.protocol.ProtocolUtils;

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

    public static OutboundPacket playerData() {
        return buffer -> {
            buffer.writeShortLE(PACKET_ID);
            buffer.writeByte(0);

            // Server Info
            ProtocolUtils.writePString(buffer, "US852"); // server version
            ProtocolUtils.writePString(buffer, "hexserver_dev"); // server name

            buffer.writeShortLE(0xFFFF); // room id?

            // User info
            writeFixedSizeString(buffer, "hexagon", 22); // username
            writeFixedSizeString(buffer, "Hex agon", 22); // nickname
            writeFixedSizeString(buffer, "guildname", 17);
            writeFixedSizeString(buffer, "guildimg", 24);
            buffer.writeIntLE(10); // connection id?
            buffer.writeZero(12);
            buffer.writeIntLE(0);
            buffer.writeIntLE(0);
            buffer.writeShortLE(0);
            buffer.writeZero(6);
            buffer.writeZero(16);
            writeFixedSizeString(buffer, "guildimg", 128);
            buffer.writeIntLE(1335); // player id

            // Player Statistics
            buffer.writeIntLE(0); // shots
            buffer.writeIntLE(0); // putt
            buffer.writeIntLE(0); // time
            buffer.writeIntLE(0); // time per shot
            buffer.writeFloatLE(0); // longest shot
            buffer.writeIntLE(0); // pangya rate
            buffer.writeIntLE(0); // timeouts
            buffer.writeIntLE(0); // out of bounds
            buffer.writeIntLE(0); // out of bounds
            buffer.writeIntLE(0); // total distance
            buffer.writeIntLE(0); // holes
            buffer.writeIntLE(0); // hole in ones
            buffer.writeShortLE(0); // bunker?
            buffer.writeIntLE(0); // fairway
            buffer.writeIntLE(0); // albatross
            buffer.writeIntLE(0); // ?
            buffer.writeIntLE(0); // putt in
            buffer.writeFloatLE(0); // longest putt
            buffer.writeFloatLE(0); // best chip in
            buffer.writeIntLE(0); // experience
            buffer.writeByte(0); // level
            buffer.writeLongLE(4456); // pang earned
            buffer.writeIntLE(0); // median score?
            buffer.writeZero(5); // best per star?
            buffer.writeByte(0); // event flag?
            buffer.writeLongLE(0);
            buffer.writeLongLE(0);
            buffer.writeLongLE(0);
            buffer.writeLongLE(0);
            buffer.writeLongLE(0);
            buffer.writeLongLE(0);
            buffer.writeIntLE(0); // played?
            buffer.writeIntLE(0); // team hole?
            buffer.writeIntLE(0); // team win?
            buffer.writeIntLE(0); // team game?
            buffer.writeIntLE(0); // ladder point?
            buffer.writeIntLE(0); // ladder hole?
            buffer.writeIntLE(0); // ladder win?
            buffer.writeIntLE(0); // ladder lose?
            buffer.writeIntLE(0); // ladder draw?
            buffer.writeIntLE(0); // combo?
            buffer.writeIntLE(0); // all combo?
            buffer.writeIntLE(0); // quit?
            buffer.writeIntLE(0);
            buffer.writeIntLE(0);
            buffer.writeIntLE(0);
            buffer.writeIntLE(0);
            buffer.writeIntLE(0);
            buffer.writeIntLE(0);
            buffer.writeZero(10);
            buffer.writeIntLE(0); // game count
            buffer.writeZero(8);

            // trophies
            // Starting from Amateur 6 to Amateur 1 repeat qualities gold, silver, bronze
            // Then from pro 1 to pro 7 repeat qualities gold, silver bronze
            buffer.writeShortLE(1);
            buffer.writeZero(76);

            // Player equipment
            new EquipmentPacket().encode(buffer);

            // Season historical stats
            for (int i = 0; i < 12; i++) { // for each season...
                for (int j = 0; j < 21; j++) { // for each course...
                    buffer.writeByte(j);
                    buffer.writeIntLE(0);
                    buffer.writeIntLE(0);
                    buffer.writeIntLE(0);
                    buffer.writeIntLE(0);
                    buffer.writeIntLE(0);
                    buffer.writeIntLE(0);
                    buffer.writeIntLE(0);
                    buffer.writeByte(0);
                    buffer.writeIntLE(0);
                    buffer.writeIntLE(0);
                    buffer.writeIntLE(0);
                    buffer.writeByte(0);
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
            buffer.writeZero(5);
            writeFixedSizeString(buffer, "mascot1", 16);
            buffer.writeZero(33);
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
