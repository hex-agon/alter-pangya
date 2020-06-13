package work.fking.pangya.packet.inbound;

import io.netty.buffer.ByteBuf;
import io.netty.util.AttributeMap;
import lombok.Getter;
import lombok.ToString;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.ProtocolUtils;

@Getter
@ToString
public class LoginRequestPacket implements InboundPacket {

    private static final int PADDING_LENGTH = 17;

    private String username;
    private String passwordMd5;

    @Override
    public void decode(ByteBuf buffer, AttributeMap attributes) {
        username = ProtocolUtils.readPString(buffer);
        passwordMd5 = ProtocolUtils.readPString(buffer);
        buffer.skipBytes(PADDING_LENGTH);
    }
}
