package work.fking.pangya.networking.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.log4j.Log4j2;
import org.anarres.lzo.LzoAlgorithm;
import org.anarres.lzo.LzoCompressor;
import org.anarres.lzo.LzoLibrary;
import org.anarres.lzo.LzoOutputStream;
import work.fking.pangya.networking.crypt.PangCrypt;

import java.io.IOException;

@Log4j2
public class ProtocolEncoder extends MessageToByteEncoder<OutboundPacket> {

    private final LzoCompressor compressor = LzoLibrary.getInstance().newCompressor(LzoAlgorithm.LZO1X, null);

    @Override
    protected void encode(ChannelHandlerContext ctx, OutboundPacket packet, ByteBuf buffer) throws IOException {
        LOGGER.trace("Encoding packet={}", packet.getClass().getSimpleName());
        // TODO: Everything here is terribly inefficient
        ByteBuf pktBuffer = ctx.alloc().buffer();
        packet.encode(pktBuffer, ctx.channel());

        LOGGER.trace("payload={}", ByteBufUtil.hexDump(pktBuffer));

        ByteBufOutputStream outputStream = new ByteBufOutputStream(ctx.alloc().buffer());
        LzoOutputStream stream = new LzoOutputStream(outputStream, compressor, 1024);
        pktBuffer.readBytes(stream, pktBuffer.readableBytes());
        stream.flush();

        ByteBuf compressed = outputStream.buffer();
        LOGGER.trace("payloadCompressed={}", ByteBufUtil.hexDump(compressed));
        compressed.skipBytes(8); // the lzo lib writes the uncompressedLength + compressedLength to the header and we don't want it

        PangCrypt.encrypt(buffer, compressed, compressed.readableBytes(), 0, 0);
        LOGGER.trace("encrypted={}", ByteBufUtil.hexDump(buffer));
    }
}
