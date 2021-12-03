package work.fking.pangya.networking.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.fking.pangya.networking.crypt.PangCrypt;
import work.fking.pangya.networking.lzo.MiniLZO;

public class ProtocolEncoder extends MessageToByteEncoder<OutboundPacket> {

    private static final Logger LOGGER = LogManager.getLogger(ProtocolEncoder.class);

    private final byte[] lzoOutBuffer = new byte[32768];
    private final int[] lzoDict = new int[1 << 14];

    private final int cryptKey;

    private ProtocolEncoder(int cryptKey) {
        this.cryptKey = cryptKey;
    }

    public static ProtocolEncoder create(int cryptKey) {
        return new ProtocolEncoder(cryptKey);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, OutboundPacket packet, ByteBuf buffer) {
        LOGGER.trace("Encoding packet={}", packet.getClass().getSimpleName());
        // TODO: Everything here is terribly inefficient
        ByteBuf pktBuffer = ctx.alloc().buffer();
        try {
            packet.encode(pktBuffer);

            int uncompressedSize = pktBuffer.readableBytes();
            byte[] source = new byte[uncompressedSize];
            pktBuffer.readBytes(source);
            var compressedSize = MiniLZO.lzo1x_compress(source, source.length, lzoOutBuffer, lzoDict);

            ByteBuf compressed = Unpooled.wrappedBuffer(lzoOutBuffer, 0, compressedSize);
            try {
                PangCrypt.encrypt(buffer, compressed, uncompressedSize, cryptKey, 0);
            } finally {
                compressed.release();
            }
        } finally {
            pktBuffer.release();
        }
    }
}
