package work.fking.pangya.packet.inbound;

import io.netty.buffer.ByteBuf;
import io.netty.util.AttributeMap;
import lombok.Getter;
import lombok.ToString;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.ProtocolUtils;

@Getter
@ToString
public class ReconnectPacket implements InboundPacket {

    private String username;
    private int userId;
    private String sessionToken;

    @Override
    public void decode(ByteBuf buffer, AttributeMap attributes) {
        username = ProtocolUtils.readPString(buffer);
        userId = buffer.readIntLE();
        sessionToken = ProtocolUtils.readPString(buffer);
    }
}
