package work.fking.pangya.networking.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ProtocolEncoder extends MessageToByteEncoder<OutboundPacket> {

    @Override
    protected void encode(ChannelHandlerContext ctx, OutboundPacket packet, ByteBuf buffer) {
        packet.encode(buffer, ctx.channel());
    }
}
