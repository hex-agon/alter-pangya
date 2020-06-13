package work.fking.pangya.packet.outbound;

import io.netty.buffer.ByteBuf;
import io.netty.util.AttributeMap;
import work.fking.pangya.networking.protocol.OutboundPacket;

public class HelloPacket implements OutboundPacket {

    private final byte[] unknown = {0x00, 0x0B, 0x00, 0x00, 0x00, 0x00};
    private final byte[] unknown2 = {0x00, 0x00, 0x00, 0x0F, 0x27, 0x00, 0x00};
    private final int cryptKey;

    private HelloPacket(int cryptKey) {
        this.cryptKey = cryptKey;
    }

    public static HelloPacket create(int cryptKey) {
        return new HelloPacket(cryptKey);
    }

    @Override
    public void encode(ByteBuf buffer, AttributeMap attributeMap) {
        buffer.writeBytes(unknown);
        buffer.writeByte(cryptKey);
        buffer.writeBytes(unknown2);
    }
}
