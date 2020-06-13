package work.fking.pangya.networking.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.util.AttributeMap;

public interface OutboundPacket extends Packet {

    /**
     * Writes this packet information to the given buffer. Note that this method must <b>NOT<b/> write any packet identification number nor any var byte/short size bytes.
     *
     * @param buffer       The buffer that the payload will be written to.
     * @param attributeMap The context.
     */
    void encode(ByteBuf buffer, AttributeMap attributeMap);
}
