package work.fking.pangya.networking.protocol;

import io.netty.buffer.ByteBuf;

public interface OutboundPacket {

    void encode(ByteBuf target);
}
