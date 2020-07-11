package work.fking.pangya.packet.outbound;

import io.netty.buffer.ByteBuf;
import io.netty.util.AttributeMap;
import work.fking.pangya.networking.protocol.OutboundPacket;
import work.fking.pangya.networking.protocol.ProtocolUtils;

public class LoginResultPacket implements OutboundPacket {

    private static final int ID = 1;

    private final Result result;

    private LoginResultPacket(Result result) {
        this.result = result;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public void encode(ByteBuf buffer, AttributeMap attributeMap) {
        buffer.writeShortLE(ID);
        buffer.writeByte(result.code);
    }

    public enum Result {
        SUCCESS(0),
        SET_NICKNAME(217),
        SELECT_CHARACTER(218),
        ERROR(227);

        private final int code;

        Result(int code) {
            this.code = code;
        }
    }

    public enum ErrorCode {
        ALREADY_LOGGED_IN(5100019),
        DUPLICATE_CONNECTION(5100107),
        INVALID_CREDENTIALS(5100143),
        BAD_RECONNECT_TOKEN(5157002);

        private final int code;

        ErrorCode(int code) {
            this.code = code;
        }
    }

    public static class Builder {

        private Builder() {
        }

        public SuccessBuilder success() {
            return new SuccessBuilder();
        }

        public ErrorBuilder error() {
            return new ErrorBuilder();
        }

        public LoginResultPacket requestNickname() {
            return new RequestNicknameLoginResultPacket();
        }

        public LoginResultPacket selectCharacter() {
            return new LoginResultPacket(Result.SELECT_CHARACTER);
        }
    }

    private static class SuccessLoginResultPacket extends LoginResultPacket {

        private final int userId;
        private final String username;
        private final String nickname;
        private final byte[] unknown = new byte[14];

        private SuccessLoginResultPacket(int userId, String username, String nickname) {
            super(Result.SUCCESS);
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

        private final ErrorCode errorCode;

        private ErrorLoginResultPacket(ErrorCode errorCode) {
            super(Result.ERROR);
            this.errorCode = errorCode;
        }

        @Override
        public void encode(ByteBuf buffer, AttributeMap attributeMap) {
            super.encode(buffer, attributeMap);
            buffer.writeIntLE(errorCode.code);
        }
    }

    public static class ErrorBuilder {

        private ErrorCode errorCode;

        public ErrorBuilder errorCode(ErrorCode errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public LoginResultPacket build() {

            if (errorCode == null) {
                throw new IllegalStateException("The error code is required");
            }
            return new ErrorLoginResultPacket(errorCode);
        }
    }

    private static class RequestNicknameLoginResultPacket extends LoginResultPacket {

        private final byte[] unknown = new byte[] {-1, -1, -1, -1};

        private RequestNicknameLoginResultPacket() {
            super(Result.SET_NICKNAME);
        }

        @Override
        public void encode(ByteBuf buffer, AttributeMap attributeMap) {
            super.encode(buffer, attributeMap);
            buffer.writeBytes(unknown);
        }
    }
}
