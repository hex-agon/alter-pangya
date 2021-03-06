package work.fking.pangya.game;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import lombok.extern.log4j.Log4j2;
import work.fking.common.Rand;
import work.fking.pangya.game.packet.outbound.HelloPacket;
import work.fking.pangya.networking.crypt.PangCrypt;
import work.fking.pangya.networking.protocol.InboundPacketDispatcher;
import work.fking.pangya.networking.protocol.Protocol;
import work.fking.pangya.networking.protocol.ProtocolDecoder;
import work.fking.pangya.networking.protocol.ProtocolEncoder;

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
        int cryptKey = Rand.withMax(PangCrypt.CRYPT_KEY_MAX);
        LOGGER.debug("New connection from {}, selected cryptKey={}", ctx.channel().remoteAddress(), cryptKey);
        ctx.writeAndFlush(HelloPacket.create(cryptKey));
        reorderPipeline(ctx.pipeline(), cryptKey);
    }

    private void reorderPipeline(ChannelPipeline pipeline, int cryptKey) {
        pipeline.remove(this);
        pipeline.replace("encoder", "encoder", ProtocolEncoder.create(cryptKey));
        pipeline.addLast("decoder", ProtocolDecoder.create(protocol, cryptKey));
        pipeline.addLast("packetDispatcher", packetDispatcher);
    }
}
