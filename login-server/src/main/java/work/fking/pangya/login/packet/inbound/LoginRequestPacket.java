package work.fking.pangya.login.packet.inbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.login.packet.handler.LoginPacketHandler;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.Packet;
import work.fking.pangya.networking.protocol.PacketFactory;
import work.fking.pangya.networking.protocol.ProtocolUtils;

@Packet(id = 0x1, handledBy = LoginPacketHandler.class)
public record LoginRequestPacket(String username, char[] passwordMd5) implements InboundPacket {

    @PacketFactory
    public static InboundPacket decode(ByteBuf buffer) {
        String username = ProtocolUtils.readPString(buffer);
        char[] passwordMd5 = ProtocolUtils.readPStringCharArray(buffer);
        buffer.skipBytes(17);

        return new LoginRequestPacket(username, passwordMd5);
    }
}
