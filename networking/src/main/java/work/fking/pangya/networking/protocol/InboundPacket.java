package work.fking.pangya.networking.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.util.AttributeMap;

public interface InboundPacket extends Packet {

    void decode(ByteBuf buffer, AttributeMap attributes);
}
