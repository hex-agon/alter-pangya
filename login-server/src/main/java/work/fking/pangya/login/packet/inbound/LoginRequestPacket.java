package work.fking.pangya.login.packet.inbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.login.packet.handler.LoginPacketHandler;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.PacketHandledBy;
import work.fking.pangya.networking.protocol.PacketId;
import work.fking.pangya.networking.protocol.ProtocolUtils;

@PacketId(0x1)
@PacketHandledBy(LoginPacketHandler.class)
public record LoginRequestPacket(String username, char[] passwordMd5) implements InboundPacket {

    public static InboundPacket decode(ByteBuf buffer) {
        String username = ProtocolUtils.readPString(buffer);
        char[] passwordMd5 = ProtocolUtils.readPStringCharArray(buffer);
        buffer.skipBytes(17);

        return new LoginRequestPacket(username, passwordMd5);
    }
}
