package work.fking.pangya.networking.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
public class ProtocolDecoder extends ByteToMessageDecoder {

    private final Protocol protocol;

    public ProtocolDecoder(Protocol protocol) {
        this.protocol = protocol;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) {
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
