package work.fking.pangya.networking.lzo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.anarres.lzo.LzoAlgorithm;
import org.anarres.lzo.LzoCompressor;
import org.anarres.lzo.LzoLibrary;
import org.anarres.lzo.LzoOutputStream;

import java.io.IOException;
import java.util.List;

public class Lzo1xEncoder extends MessageToMessageEncoder<ByteBuf> {

    private final LzoCompressor compressor = LzoLibrary.getInstance().newCompressor(LzoAlgorithm.LZO1X, null);

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws IOException {
        ByteBufOutputStream outputStream = new ByteBufOutputStream(ctx.alloc().buffer());
        LzoOutputStream stream = new LzoOutputStream(outputStream, compressor, 4096);
        msg.readBytes(stream, msg.readableBytes());
        stream.flush();
        ByteBuf buffer = outputStream.buffer();
        buffer.skipBytes(8); // the lzo lib writes the uncompressedLength + compressedLength to the header and we don't want it
        out.add(buffer);
    }
}
