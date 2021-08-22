package work.fking.pangya.login.packet.outbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.crypt.PangCrypt;
import work.fking.pangya.networking.protocol.OutboundPacket;

public class HelloPacket implements OutboundPacket {

    private final int cryptKey;

    private HelloPacket(int cryptKey) {
        this.cryptKey = cryptKey;
    }

    public static HelloPacket create(int cryptKey) {
        if (cryptKey < 0 || cryptKey > PangCrypt.CRYPT_KEY_MAX) {
            throw new IllegalArgumentException("Crypt key out of bounds");
        }
        return new HelloPacket(cryptKey);
    }

    @Override
    public void encode(ByteBuf target) {
        target.writeShortLE(0xb00);
        target.writeZero(4);
        target.writeByte(cryptKey);
        target.writeZero(3);
        target.writeIntLE(10101); // suspicious value, similar to ports used by login servers
    }
}
