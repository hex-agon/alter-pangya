package work.fking.pangya.login.packet.outbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.protocol.OutboundPacket;
import work.fking.pangya.networking.protocol.ProtocolUtils;

public class SessionKeyPacket implements OutboundPacket {

    private static final int ID = 3;

    private final String sessionKey;

    private SessionKeyPacket(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public static SessionKeyPacket create(String sessionKey) {
        return new SessionKeyPacket(sessionKey);
    }

    @Override
    public void encode(ByteBuf target) {
        target.writeShortLE(ID);
        target.writeIntLE(0); // 4 dummy bytes
        ProtocolUtils.writePString(target, sessionKey);
    }
}
