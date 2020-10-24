package work.fking.pangya.login.packet.inbound;

import io.netty.buffer.ByteBuf;
import io.netty.util.AttributeMap;
import lombok.Getter;
import lombok.ToString;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.ProtocolUtils;

@Getter
@ToString
public class SetNicknamePacket implements InboundPacket {

    private String nickname;

    @Override
    public void decode(ByteBuf buffer, AttributeMap attributes) {
        nickname = ProtocolUtils.readPString(buffer);
    }
}
