package work.fking.pangya;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import lombok.extern.log4j.Log4j2;
import work.fking.pangya.networking.protocol.ProtocolEncoder;
import work.fking.pangya.networking.protocol.Protocol;
import work.fking.pangya.networking.protocol.ProtocolDecoder;
import work.fking.pangya.packet.outbound.HelloPacket;

@Log4j2
@Sharable
public class HelloHandler extends ChannelInboundHandlerAdapter {

    private final Protocol protocol;

    private HelloHandler(Protocol protocol) {
        this.protocol = protocol;
    }

    public static HelloHandler create(Protocol protocol) {
        return new HelloHandler(protocol);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        LOGGER.trace("New connection from {}", ctx.channel().remoteAddress());
        ctx.writeAndFlush(HelloPacket.create(0));

        ChannelPipeline pipeline = ctx.pipeline();
        pipeline.remove(this);
        pipeline.remove("encoder");
        pipeline.addLast(new ProtocolDecoder(protocol));
        pipeline.addLast(new PacketHandler());
        pipeline.addFirst(new ProtocolEncoder());
    }
}
