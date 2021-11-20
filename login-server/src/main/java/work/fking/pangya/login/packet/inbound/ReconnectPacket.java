package work.fking.pangya.login.packet.inbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.login.packet.handler.ReconnectPacketHandler;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.Packet;
import work.fking.pangya.networking.protocol.PacketFactory;
import work.fking.pangya.networking.protocol.ProtocolUtils;

@Packet(id = 0xb, handledBy = ReconnectPacketHandler.class)
public record ReconnectPacket(String username, int userId, String loginKey) implements InboundPacket {

    @PacketFactory
    public static InboundPacket decode(ByteBuf buffer) {
        String username = ProtocolUtils.readPString(buffer);
        int userId = buffer.readIntLE();
        String loginKey = ProtocolUtils.readPString(buffer);

        return new ReconnectPacket(username, userId, loginKey);
    }
}
