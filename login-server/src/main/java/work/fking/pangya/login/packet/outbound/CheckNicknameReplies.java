package work.fking.pangya.login.packet.outbound;

import work.fking.pangya.networking.protocol.OutboundPacket;
import work.fking.pangya.networking.protocol.ProtocolUtils;

public final class CheckNicknameReplies {

    private static final int PACKET_ID = 0xE;

    private CheckNicknameReplies() {
    }

    public static OutboundPacket available(String nickname) {
        return buffer -> {
            buffer.writeShortLE(PACKET_ID);
            buffer.writeIntLE(0);
            ProtocolUtils.writePString(buffer, nickname);
        };
    }

    public static OutboundPacket error(Error error) {
        return buffer -> {
            buffer.writeShortLE(PACKET_ID);
            buffer.writeIntLE(error.code);
        };
    }

    public enum Error {
        ERROR(1),
        IN_USE(3),
        INCORRECT_FORMAT_OR_LENGTH(4),
        NOT_ENOUGH_POINTS(5),
        INAPPROPRIATE_WORDS(6),
        DATABASE_ERROR(7),
        SAME_NICKNAME_WILL_BE_USED(9);

        private final int code;

        Error(int code) {
            this.code = code;
        }
    }
}
