package work.fking.pangya.login.packet.inbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.PacketFactory;
import work.fking.pangya.networking.protocol.ProtocolUtils;

public record ReconnectPacket(String username, int userId, String loginKey) implements InboundPacket {

    @PacketFactory
    public static InboundPacket decode(ByteBuf buffer) {
        String username = ProtocolUtils.readPString(buffer);
        int userId = buffer.readIntLE();
        String loginKey = ProtocolUtils.readPString(buffer);

        return new ReconnectPacket(username, userId, loginKey);
    }
}
