package work.fking.pangya.login.packet.inbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.PacketFactory;
import work.fking.pangya.networking.protocol.ProtocolUtils;

public record LoginRequestPacket(String username, char[] passwordMd5) implements InboundPacket {

    private static final int PADDING_LENGTH = 17;

    @PacketFactory
    public static InboundPacket decode(ByteBuf buffer) {
        String username = ProtocolUtils.readPString(buffer);
        char[] passwordMd5 = ProtocolUtils.readPStringCharArray(buffer);
        buffer.skipBytes(PADDING_LENGTH);

        return new LoginRequestPacket(username, passwordMd5);
    }
}
