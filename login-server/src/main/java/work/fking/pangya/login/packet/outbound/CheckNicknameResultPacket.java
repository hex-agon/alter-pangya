package work.fking.pangya.login.packet.outbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.protocol.OutboundPacket;
import work.fking.pangya.networking.protocol.ProtocolUtils;

public class CheckNicknameResultPacket implements OutboundPacket {

    private static final int ID = 14;
    private final Result result;

    private CheckNicknameResultPacket(Result result) {
        this.result = result;
    }

    public static CheckNicknameResultPacket available(String nickname) {
        return new AvailableNicknamePacket(nickname);
    }

    public static CheckNicknameResultPacket error(Result result) {
        return new CheckNicknameResultPacket(result);
    }

    @Override
    public void encode(ByteBuf target) {
        target.writeShortLE(ID);
        target.writeIntLE(result.code);
    }

    public enum Result {
        AVAILABLE(0),
        ERROR(1),
        IN_USE(3),
        INCORRECT_FORMAT_OR_LENGTH(4),
        NOT_ENOUGH_POINTS(5),
        INAPPROPRIATE_WORDS(6),
        DATABASE_ERROR(7),
        SAME_NICKNAME_WILL_BE_USED(9);

        private final int code;

        Result(int code) {
            this.code = code;
        }
    }

    private static class AvailableNicknamePacket extends CheckNicknameResultPacket {

        private final String nickname;

        private AvailableNicknamePacket(String nickname) {
            super(Result.AVAILABLE);
            this.nickname = nickname;
        }

        @Override
        public void encode(ByteBuf target) {
            super.encode(target);
            ProtocolUtils.writePString(target, nickname);
        }
    }
}
