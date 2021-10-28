package work.fking.pangya.game.packet.outbound;

import io.netty.buffer.ByteBufUtil;
import work.fking.pangya.networking.protocol.OutboundPacket;
import work.fking.pangya.networking.protocol.ProtocolUtils;

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
        // https://github.com/Acrisio-Filho/SuperSS-Dev/blob/747ac1615c849ed13526415105edd0b4ebd7bc6c/Server%20Lib/Game%20Server/PACKET/packet_func_sv.cpp#L6158
        return buffer -> {
            buffer.writeShortLE(PACKET_ID);
            buffer.writeByte(0);

            // Server Info
            ProtocolUtils.writePString(buffer, "US852"); // server version
            ProtocolUtils.writePString(buffer, "hexserver_dev"); // server name
            // Server Info

            // Player info (TPlayerInfo1 https://github.com/hsreina/pangya-server/blob/develop/src/Server/Game/PlayerData.pas#L19)
            buffer.writeShortLE(0xFFFF); // room id?
            ProtocolUtils.writeFixedSizeString(buffer, "hexagon", 22); // username
            ProtocolUtils.writeFixedSizeString(buffer, "Hex agon", 22); // nickname
            ProtocolUtils.writeFixedSizeString(buffer, "guildname", 17);
            ProtocolUtils.writeFixedSizeString(buffer, "guildimg", 9);
            // next byte here technically is the gm flag (0xf)
            buffer.writeZero(7);
            buffer.writeIntLE(0); // player related, unknown
            buffer.writeByte(0);
            buffer.writeIntLE(1335); // connectionId
            buffer.writeZero(12);
            buffer.writeIntLE(0); // guild id
            buffer.writeIntLE(0);
            buffer.writeShortLE(0); // player sex?
            buffer.writeIntLE(0xFFFFFFFF); // 6 bytes
            buffer.writeShortLE(0xFFFF); // 6 bytes
            buffer.writeZero(12);
            ProtocolUtils.writeFixedSizeString(buffer, "domain", 18);
            buffer.writeZero(114);
            buffer.writeIntLE(1); // user id?

            // Player Statistics
            var statistics = ByteBufUtil.decodeHexDump("""
                    00000000000000009B04000000000000000000000000000000000000000
                    00000000000000000000000000000000000000000000000000000000000
                    000000000000000000000000000000000000000020F40E0000000000000
                    00000000000000000000000000000000000000000000000000000000000
                    00000000000000000000000000000000000000000000000000000005000
                    000000000000000000000000000E8030000000000000000000000000000
                    00000000000000000000000004000000000000000000000000000000000
                    0000000000000FFFFFFFF00000000000000000000000000000000000000
                    000000
                    """.replace("\n", ""));
            buffer.writeBytes(statistics);

            // Trohpies
//            buffer.writeZero(0x4c);
//
//            // Player equipment
//            new EquipmentPacket().encode(buffer);
//
//            // unknown
//            buffer.writeZero(0x2a54);
//
//            // Active Character
//            PangCharacter.mock().encode(buffer);
//
//            // Active Caddie
//            PangCaddie.mock().encode(buffer);
//
//            // Active Clubset
//            buffer.writeIntLE(2000); // item unique id
//            buffer.writeIntLE(268435511); // item iff id
//            buffer.writeByte(5); // power slot
//            buffer.writeByte(4); // control slot
//            buffer.writeByte(3); // accuracy slot
//            buffer.writeByte(2); // spin slot
//            buffer.writeByte(1); // curve slot
//            buffer.writeShortLE(1); // power upgrades?
//            buffer.writeShortLE(1); // control upgrades?
//            buffer.writeShortLE(1); // accuracy upgrades?
//            buffer.writeShortLE(1); // spin upgrades?
//            buffer.writeShortLE(1); // curve upgrades?
//
//            // Active Mascot
//            buffer.writeIntLE(0); // item unique id
//            buffer.writeIntLE(0); // item iff id
//            buffer.writeZero(5);
//            buffer.writeZero(0x10);
//            buffer.writeZero(0x21);
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
