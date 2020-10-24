package work.fking.pangya.login;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import lombok.extern.log4j.Log4j2;
import work.fking.pangya.networking.protocol.InboundPacketDispatcher;
import work.fking.pangya.networking.protocol.Protocol;
import work.fking.pangya.networking.protocol.ProtocolDecoder;
import work.fking.pangya.networking.protocol.ProtocolEncoder;
import work.fking.pangya.login.packet.outbound.HelloPacket;

@Log4j2
@Sharable
public class HelloHandler extends ChannelInboundHandlerAdapter {

    private final Protocol protocol;
    private final InboundPacketDispatcher packetDispatcher;

    private HelloHandler(Protocol protocol, InboundPacketDispatcher packetDispatcher) {
        this.protocol = protocol;
        this.packetDispatcher = packetDispatcher;
    }

    public static HelloHandler create(Protocol protocol, InboundPacketDispatcher packetDispatcher) {
        return new HelloHandler(protocol, packetDispatcher);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        LOGGER.trace("New connection from {}", ctx.channel().remoteAddress());
        // TODO: Think on how we'll pass the cryptKey around
        ctx.writeAndFlush(HelloPacket.create(0));

        ChannelPipeline pipeline = ctx.pipeline();
        pipeline.remove(this);
        pipeline.replace("encoder", "encoder", new ProtocolEncoder());
        pipeline.addLast("decoder", new ProtocolDecoder(protocol));
        pipeline.addLast("packetDispatcher", packetDispatcher);
    }
}
