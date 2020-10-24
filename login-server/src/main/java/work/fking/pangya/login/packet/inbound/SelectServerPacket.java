package work.fking.pangya.login.packet.inbound;

import io.netty.buffer.ByteBuf;
import io.netty.util.AttributeMap;
import lombok.Getter;
import lombok.ToString;
import work.fking.pangya.networking.protocol.InboundPacket;

@Getter
@ToString
public class SelectServerPacket implements InboundPacket {

    private int serverId;

    @Override
    public void decode(ByteBuf buffer, AttributeMap attributes) {
        serverId = buffer.readShortLE();
        buffer.skipBytes(2);
    }
}
