package work.fking.pangya.login.packet.outbound;

import io.netty.buffer.ByteBuf;
import io.netty.util.AttributeMap;
import work.fking.pangya.networking.protocol.OutboundPacket;
import work.fking.pangya.networking.protocol.ProtocolUtils;

public class LoginResultPacket implements OutboundPacket {

    private static final int ID = 1;

    private final int resultCode;

    private LoginResultPacket(int resultCode) {
        this.resultCode = resultCode;
    }

    public static SuccessBuilder success() {
        return new SuccessBuilder();
    }

    public static ErrorBuilder error(Result result) {
        return new ErrorBuilder(result);
    }

    @Override
    public void encode(ByteBuf buffer, AttributeMap attributeMap) {
        buffer.writeShortLE(ID);
        buffer.writeByte(resultCode);
    }

    private static class SuccessLoginResultPacket extends LoginResultPacket {

        private final int userId;
        private final String username;
        private final String nickname;
        private final byte[] unknown = new byte[14];

        private SuccessLoginResultPacket(int userId, String username, String nickname) {
            super(Result.SUCCESS.code);
            this.userId = userId;
            this.username = username;
            this.nickname = nickname;
        }

        @Override
        public void encode(ByteBuf buffer, AttributeMap attributeMap) {
            super.encode(buffer, attributeMap);

            ProtocolUtils.writePString(buffer, username);
            buffer.writeIntLE(userId);
            buffer.writeBytes(unknown);
            ProtocolUtils.writePString(buffer, nickname);
        }
    }

    public static class SuccessBuilder {

        private int userId = -1;
        private String username;
        private String nickname;

        public SuccessBuilder userId(int userId) {
            this.userId = userId;
            return this;
        }

        public SuccessBuilder username(String username) {
            this.username = username;
            return this;
        }

        public SuccessBuilder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public LoginResultPacket build() {

            if (userId <= -1) {
                throw new IllegalStateException("The userId must be bigger or equal than zero");
            }

            if (username == null || username.isBlank()) {
                throw new IllegalStateException("The username is required");
            }

            if (nickname == null || nickname.isBlank()) {
                throw new IllegalStateException("The nickname is required");
            }
            return new SuccessLoginResultPacket(userId, username, nickname);
        }
    }

    private static class ErrorLoginResultPacket extends LoginResultPacket {

        private final Result result;
        private final String notice;
        private final int suspensionTime;

        private ErrorLoginResultPacket(Result result, int suspensionTime, String notice) {
            super(result.code);
            this.result = result;
            this.suspensionTime = suspensionTime;
            this.notice = notice;
        }

        @Override
        public void encode(ByteBuf buffer, AttributeMap attributeMap) {
            super.encode(buffer, attributeMap);

            if (suspensionTime > 0) {
                buffer.writeIntLE(suspensionTime);
            }

            if (result.allowsNotice) {
                ProtocolUtils.writePString(buffer, notice);
            }
        }
    }

    public static class ErrorBuilder {

        private final Result result;
        private int suspensionTime;
        private String notice;

        public ErrorBuilder(Result result) {
            this.result = result;
            this.notice = "";
        }

        /**
         * A notice popup message to display
         *
         * @param notice The notice popup message
         */
        public ErrorBuilder notice(String notice) {
            this.notice = notice;
            return this;
        }

        /**
         * For how long the user account is suspended for in hours.
         *
         * @param suspensionTime The suspension time in hours
         */
        public ErrorBuilder suspensionTime(int suspensionTime) {
            this.suspensionTime = suspensionTime;
            return this;
        }

        public LoginResultPacket build() {

            if (!notice.isEmpty() && !result.allowsNotice) {
                throw new IllegalStateException("This result doesn't allow a notice message");
            }

            if (result == Result.ACCOUNT_SUSPENDED) {

                if (suspensionTime <= 0) {
                    throw new IllegalStateException("Suspension time is required for the ACCOUNT_SUSPENDED result and must be bigger than 0");
                }
            } else {

                if (suspensionTime > 0) {
                    throw new IllegalStateException("Suspension time is only allowed for the ACCOUNT_SUSPENDED result");
                }
            }
            return new ErrorLoginResultPacket(result, suspensionTime, notice);
        }
    }

    public enum Result {
        SUCCESS(0x0),
        INVALID_ID_PW(0x1),
        INVALID_ID(0x2),
        USERNAME_IN_USE(0x4, false), // actually accepts notice but it glitches out
        BANNED(0x5),
        INCORRECT_USERNAME_PASSWORD(0x6),
        ACCOUNT_SUSPENDED(0x7),
        PLAYER_UNDER_AGE(0x9),
        INCORRECT_SSN(0xC),
        INCORRECT_USERNAME(0xD),
        WHITELISTED_USERS_ONLY(0xE),
        SERVER_MAINTENANCE(0xF),
        GEO_BLOCKED(0x10),
        CREATE_NICKNAME(0xD8, false),
        SELECT_CHARACTER(0xD9, false);

        private final int code;
        private final boolean allowsNotice;

        Result(int code) {
            this.code = code;
            this.allowsNotice = true;
        }

        Result(int code, boolean allowsNotice) {
            this.code = code;
            this.allowsNotice = allowsNotice;
        }
    }
}
