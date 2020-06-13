package work.fking.pangya.networking.protocol;

import io.netty.channel.ChannelHandlerContext;

public interface InboundPacketHandler<P extends InboundPacket> {

    void handle(ChannelHandlerContext ctx, P packet);
}
