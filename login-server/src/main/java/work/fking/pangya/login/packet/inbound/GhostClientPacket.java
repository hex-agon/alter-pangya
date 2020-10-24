package work.fking.pangya.login.packet.inbound;

import io.netty.buffer.ByteBuf;
import io.netty.util.AttributeMap;
import lombok.ToString;
import work.fking.pangya.networking.protocol.InboundPacket;

@ToString
public class GhostClientPacket implements InboundPacket {

    @Override
    public void decode(ByteBuf buffer, AttributeMap attributes) {
    }
}
