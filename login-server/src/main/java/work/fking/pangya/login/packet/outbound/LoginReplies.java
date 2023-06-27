package work.fking.pangya.login.packet.outbound;

import work.fking.pangya.networking.protocol.OutboundPacket;
import work.fking.pangya.networking.protocol.ProtocolUtils;

import static work.fking.pangya.networking.protocol.ProtocolUtils.writeFixedSizeString;
import static work.fking.pangya.networking.protocol.ProtocolUtils.writePString;

public final class LoginReplies {

    private static final int RESULT_PACKET_ID = 0x1;
    private static final int SESSION_KEY_PACKET_ID = 0x3;
    private static final int CHAT_MACROS_PACKET_ID = 0x6;
    private static final int LOGIN_KEY_PACKET_ID = 0x10;

    private LoginReplies() {
    }

    public static OutboundPacket loginKey(String loginKey) {
        return buffer -> {
            buffer.writeShortLE(LOGIN_KEY_PACKET_ID);
            writePString(buffer, loginKey);
        };
    }

    public static OutboundPacket sessionKey(String sessionKey) {
        return buffer -> {
            buffer.writeShortLE(SESSION_KEY_PACKET_ID);
            buffer.writeZero(4);
            writePString(buffer, sessionKey);
        };
    }

    public static OutboundPacket chatMacros() {
        return buffer -> {
            buffer.writeShortLE(CHAT_MACROS_PACKET_ID);

            for (int i = 0; i < 9; i++) {
                writeFixedSizeString(buffer, "Welcome to PangYa!", 64);
            }
        };
    }

    public static OutboundPacket success(int uid, String username, String nickname) {
        return buffer -> {
            buffer.writeShortLE(RESULT_PACKET_ID);
            buffer.writeByte(0); // success
            writePString(buffer, username);
            buffer.writeIntLE(uid);
            buffer.writeZero(14);
            writePString(buffer, nickname);
        };
    }

    public static OutboundPacket error(Error error) {
        return error(error, "");
    }

    public static OutboundPacket error(Error error, String message) {
        return buffer -> {
            buffer.writeShortLE(RESULT_PACKET_ID);
            buffer.writeByte(error.code);

            if (message != null) {
                writePString(buffer, message);
            }
        };
    }

    public static OutboundPacket accountSuspended(int liftTimeHours) {
        return buffer -> {
            buffer.writeShortLE(RESULT_PACKET_ID);
            buffer.writeByte(0x7);
            buffer.writeIntLE(liftTimeHours);
        };
    }

    public static OutboundPacket selectCharacter() {
        return buffer -> {
            buffer.writeShortLE(RESULT_PACKET_ID);
            buffer.writeByte(0xD9);
        };
    }

    public static OutboundPacket createNickname() {
        return buffer -> {
            buffer.writeShortLE(RESULT_PACKET_ID);
            buffer.writeByte(0xD8);
        };
    }

    public enum Error {
        INVALID_ID_PW(0x1),
        INVALID_ID(0x2),
        USERNAME_IN_USE(0x4), // actually accepts notice but it glitches out
        BANNED(0x5),
        INCORRECT_USERNAME_PASSWORD(0x6),
        PLAYER_UNDER_AGE(0x9),
        INCORRECT_SSN(0xC),
        INCORRECT_USERNAME(0xD),
        WHITELISTED_USERS_ONLY(0xE),
        SERVER_MAINTENANCE(0xF),
        GEO_BLOCKED(0x10);

        private final int code;

        Error(int code) {
            this.code = code;
        }
    }
}
