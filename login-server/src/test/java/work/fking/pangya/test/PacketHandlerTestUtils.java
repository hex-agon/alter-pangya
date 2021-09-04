package work.fking.pangya.test;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.InboundPacketHandler;

public final class PacketHandlerTestUtils {

    private PacketHandlerTestUtils() {
    }

    public static <P extends InboundPacket> SimpleChannelInboundHandler<P> channelHandlerFor(InboundPacketHandler<P> handler) {
        return new SimpleChannelInboundHandler<>() {
            @Override
            protected void channelRead0(ChannelHandlerContext ctx, P msg) {
                handler.handle(ctx.channel(), msg);
            }
        };
    }
}
