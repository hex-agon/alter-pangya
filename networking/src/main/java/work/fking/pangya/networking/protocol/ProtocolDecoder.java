package work.fking.pangya.networking.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.log4j.Log4j2;
import work.fking.pangya.networking.crypt.PangCrypt;

import java.util.List;

@Log4j2
public class ProtocolDecoder extends ByteToMessageDecoder {

    private final Protocol protocol;

    public ProtocolDecoder(Protocol protocol) {
        this.protocol = protocol;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) {
        buffer.markReaderIndex();
        buffer.skipBytes(1); // skips the first salt byte
        int payloadSize = buffer.readShortLE();
        buffer.skipBytes(1); // skips the fourth unknown byte

        int readableBytes = buffer.readableBytes();

        if (readableBytes < payloadSize) {
            LOGGER.trace("Insufficient amount of bytes, readable={}, needed={}", readableBytes, payloadSize);
            return;
        }
        buffer.resetReaderIndex();
        PangCrypt.decrypt(buffer, 0);

        int packetId = buffer.readShortLE();
        LOGGER.trace("Incoming packetId={}", packetId);

        try {
            InboundPacket inboundPacket = protocol.createInboundPacket(packetId);

            if (inboundPacket == null) {
                LOGGER.warn("Unknown packetId={}", packetId);
                ctx.disconnect();
                return;
            }
            inboundPacket.decode(buffer, ctx.channel());
            out.add(inboundPacket);
        } catch (Throwable throwable) {
            throw new ProtocolException(throwable);
        }
    }
}
