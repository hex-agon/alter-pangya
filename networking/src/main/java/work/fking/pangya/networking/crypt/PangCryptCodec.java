package work.fking.pangya.networking.crypt;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
public class PangCryptCodec extends ByteToMessageCodec<ByteBuf> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) {
        out.writeBytes(msg);
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
        out.add(buffer.readBytes(readableBytes));
    }
}
