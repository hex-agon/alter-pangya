package work.fking.pangya.game.packet.outbound;

import io.netty.buffer.ByteBuf;
import io.netty.util.AttributeMap;
import work.fking.pangya.networking.crypt.PangCrypt;
import work.fking.pangya.networking.protocol.OutboundPacket;

public class HelloPacket implements OutboundPacket {

    private final byte[] unknown = {0x06, 0x00, 0x00, 0x3F, 0x00, 0x01, 0x01};
    private final int cryptKey;

    private HelloPacket(int cryptKey) {
        this.cryptKey = cryptKey;
    }

    public static HelloPacket create(int cryptKey) {

        if (cryptKey < 0 || cryptKey > PangCrypt.CRYPT_KEY_MAX) {
            throw new IllegalArgumentException("Invalid crypt key");
        }
        return new HelloPacket(cryptKey);
    }

    @Override
    public void encode(ByteBuf buffer, AttributeMap attributeMap) {
        buffer.writeByte(0);
        buffer.writeBytes(unknown);
        buffer.writeByte(cryptKey);
    }
}
