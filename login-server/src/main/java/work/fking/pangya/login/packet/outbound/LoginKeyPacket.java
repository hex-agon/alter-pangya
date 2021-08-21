package work.fking.pangya.login.packet.outbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.protocol.OutboundPacket;
import work.fking.pangya.networking.protocol.ProtocolUtils;

public class LoginKeyPacket implements OutboundPacket {

    private static final int ID = 0x10;

    private final String loginKey;

    private LoginKeyPacket(String loginKey) {
        this.loginKey = loginKey;
    }

    public static LoginKeyPacket create(String loginKey) {
        return new LoginKeyPacket(loginKey);
    }

    @Override
    public void encode(ByteBuf target) {
        target.writeShortLE(ID);
        ProtocolUtils.writePString(target, loginKey);
    }
}
