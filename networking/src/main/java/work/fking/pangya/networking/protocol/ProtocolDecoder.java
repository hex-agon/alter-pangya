package work.fking.pangya.networking.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.fking.pangya.networking.crypt.PangCrypt;

import java.util.List;

public class ProtocolDecoder extends ByteToMessageDecoder {

    private static final Logger LOGGER = LogManager.getLogger(InboundPacketDispatcher.class);

    private final Protocol protocol;
    private final int cryptKey;

    private ProtocolDecoder(Protocol protocol, int cryptKey) {
        this.protocol = protocol;
        this.cryptKey = cryptKey;
    }

    public static ProtocolDecoder create(Protocol protocol, int cryptKey) {
        return new ProtocolDecoder(protocol, cryptKey);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) {
        PangCrypt.decrypt(buffer, cryptKey);

        int packetId = buffer.readShortLE();
        LOGGER.trace("Incoming packetId=0x{}", Integer.toHexString(packetId));

        try {
            InboundPacket inboundPacket = protocol.createInboundPacket(packetId, buffer);

            if (inboundPacket == null) {
                LOGGER.warn("Unknown packetId=0x{}", Integer.toHexString(packetId));
                LOGGER.warn("\n{}", ByteBufUtil.prettyHexDump(buffer));
                buffer.clear();
                ctx.disconnect();
                return;
            }
            out.add(inboundPacket);
        } catch (Throwable throwable) {
            throw new ProtocolException(throwable);
        }
    }
}
