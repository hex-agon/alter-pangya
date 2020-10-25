package work.fking.pangya.game.packet.inbound;

import io.netty.buffer.ByteBuf;
import io.netty.util.AttributeMap;
import lombok.Getter;
import lombok.ToString;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.ProtocolUtils;

@Getter
@ToString
public class HandoverPacket implements InboundPacket {

    private String username;
    private int userId;
    private String loginKey;
    private String clientVersion;
    private String sessionKey;

    @Override
    public void decode(ByteBuf buffer, AttributeMap attributes) {
        username = ProtocolUtils.readPString(buffer);
        userId = buffer.readIntLE();
        buffer.readIntLE();
        buffer.readShortLE();
        loginKey = ProtocolUtils.readPString(buffer);
        clientVersion = ProtocolUtils.readPString(buffer);
        buffer.readIntLE();
        buffer.readIntLE();
        sessionKey = ProtocolUtils.readPString(buffer);
    }
}
