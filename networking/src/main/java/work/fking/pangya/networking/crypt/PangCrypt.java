package work.fking.pangya.networking.crypt;

import io.netty.buffer.ByteBuf;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class PangCrypt {

    public final int CLIENT_HEADER_SIZE = 5;
    public final int SERVER_HEADER_SIZE = 8;
    private final int CRYPT_STRIDE = 4;

    public final int CRYPT_KEY_MIN = 0;
    public final int CRYPT_KEY_MAX = 0xF;

    public final int CRYPT_SALT_MIN = 0;
    public final int CRYPT_SALT_MAX = 0xFF;

    public void decrypt(ByteBuf buffer, int key) {

        if (key < CRYPT_KEY_MIN || key > CRYPT_KEY_MAX) {
            throw new PangCryptException("Key is out of range, must be 0-15");
        }

        if (buffer.readableBytes() < CLIENT_HEADER_SIZE) {
            throw new PangCryptException("Buffer is too small");
        }
        int salt = buffer.readUnsignedByte();
        int payloadSize = buffer.readUnsignedShortLE();
        int unknown = buffer.readByte();
        LOGGER.trace("Decrypt salt={}, payloadSize={}, unknown={}", salt, payloadSize, unknown);
        int cryptByte = PangCryptTables.SECOND[(key << 8) + salt];

        buffer.setByte(buffer.readerIndex(), cryptByte); // set the 5th header byte as the cryptByte
        buffer.skipBytes(1); // then skip it

        int start = buffer.readerIndex() + 3; // starting from the 8th byte (we heave already read 5) start transforming the values
        int length = payloadSize + CRYPT_STRIDE;

        for (int i = start; i < length; i++) {
            int value = buffer.getByte(i) ^ buffer.getByte(i - CRYPT_STRIDE);

            buffer.setByte(i, value);
        }
    }

    public void encrypt(ByteBuf target, ByteBuf compressedPayload, int uncompressedPayloadSize, int key, int salt) {

        if (key < CRYPT_KEY_MIN || key > CRYPT_KEY_MAX) {
            throw new PangCryptException("Key is out of range, must be 0-15");
        }

        if (salt < CRYPT_SALT_MIN || salt > CRYPT_SALT_MAX) {
            throw new PangCryptException("Salt is out of range, must be 0-255");
        }
        int payloadSize = compressedPayload.readableBytes();
        int size = payloadSize + SERVER_HEADER_SIZE - 3; // the compressedPayload size, plus our header size minus the first 3 bytes
        int tableIdx = (key << 8) + salt;
        int xorByte = PangCryptTables.FIRST[tableIdx] ^ PangCryptTables.SECOND[tableIdx];

        target.writeByte(salt);
        target.writeShortLE(size);
        target.writeByte(xorByte);
        target.writeByte(0);

        int x = (uncompressedPayloadSize + uncompressedPayloadSize / 255) & 0xFF;
        int v = (uncompressedPayloadSize - x) / 255;
        int y = (v + v / 255) & 0xFF;
        int w = (v - y) / 255;
        int z = (w + w / 255) & 0xFF;

        target.writeByte(z);
        target.writeByte(y);
        target.writeByte(x);
        target.writeBytes(compressedPayload);

        int start = target.readerIndex() + 10;
        int length = payloadSize + CRYPT_STRIDE + 3;

        for (int i = length; i >= start; i--) {
            int value = target.getByte(i) ^ target.getByte(i - CRYPT_STRIDE);

            target.setByte(i, value);
        }
        int byteIdx = target.readerIndex() + 7;
        target.setByte(byteIdx, target.getByte(byteIdx) ^ PangCryptTables.SECOND[tableIdx]);
    }
}
