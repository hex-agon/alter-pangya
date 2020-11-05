package work.fking.pangya.game.packet.inbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.PacketFactory;
import work.fking.pangya.networking.protocol.ProtocolUtils;

public record HandoverPacket(String username, int userId, String loginKey, String clientVersion, String sessionKey) implements InboundPacket {

    @PacketFactory
    public static InboundPacket decode(ByteBuf buffer) {
        String username = ProtocolUtils.readPString(buffer);
        int userId = buffer.readIntLE();
        buffer.readIntLE();
        buffer.readShortLE();
        String loginKey = ProtocolUtils.readPString(buffer);
        String clientVersion = ProtocolUtils.readPString(buffer);
        buffer.readIntLE();
        buffer.readIntLE();
        String sessionKey = ProtocolUtils.readPString(buffer);

        return new HandoverPacket(username, userId, loginKey, clientVersion, sessionKey);
    }
}
