package work.fking.pangya.login.packet.inbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.login.packet.handler.ReconnectPacketHandler;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.PacketHandledBy;
import work.fking.pangya.networking.protocol.PacketId;
import work.fking.pangya.networking.protocol.ProtocolUtils;

@PacketId(0xb)
@PacketHandledBy(ReconnectPacketHandler.class)
public record ReconnectPacket(String username, int userId, String loginKey) implements InboundPacket {

    public static InboundPacket decode(ByteBuf buffer) {
        String username = ProtocolUtils.readPString(buffer);
        int userId = buffer.readIntLE();
        String loginKey = ProtocolUtils.readPString(buffer);

        return new ReconnectPacket(username, userId, loginKey);
    }
}
