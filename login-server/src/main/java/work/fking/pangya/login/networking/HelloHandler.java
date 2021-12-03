package work.fking.pangya.login.networking;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.fking.pangya.common.Rand;
import work.fking.pangya.login.packet.outbound.HelloPacket;
import work.fking.pangya.networking.crypt.PangCrypt;
import work.fking.pangya.networking.protocol.InboundPacketDispatcher;
import work.fking.pangya.networking.protocol.Protocol;
import work.fking.pangya.networking.protocol.ProtocolDecoder;
import work.fking.pangya.networking.protocol.ProtocolEncoder;

@Sharable
public class HelloHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LogManager.getLogger(HelloHandler.class);

    private final Protocol protocol;
    private final InboundPacketDispatcher packetDispatcher;
    private final ExceptionHandler exceptionHandler;

    private HelloHandler(Protocol protocol, InboundPacketDispatcher packetDispatcher, ExceptionHandler exceptionHandler) {
        this.protocol = protocol;
        this.packetDispatcher = packetDispatcher;
        this.exceptionHandler = exceptionHandler;
    }

    public static HelloHandler create(Protocol protocol, InboundPacketDispatcher packetDispatcher, ExceptionHandler exceptionHandler) {
        return new HelloHandler(protocol, packetDispatcher, exceptionHandler);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        int cryptKey = Rand.withMax(PangCrypt.CRYPT_KEY_MAX);
        LOGGER.trace("New connection from {}, selected cryptKey={}", ctx.channel().remoteAddress(), cryptKey);

        ctx.channel().attr(ConnectionState.KEY).set(ConnectionState.AUTHENTICATING);
        ctx.writeAndFlush(HelloPacket.create(cryptKey));
        reorderPipeline(ctx.pipeline(), cryptKey);
    }

    private void reorderPipeline(ChannelPipeline pipeline, int cryptKey) {
        pipeline.remove(this);
        pipeline.replace("encoder", "encoder", ProtocolEncoder.create(cryptKey));
        pipeline.addLast("decoder", ProtocolDecoder.create(protocol, cryptKey));
        pipeline.addLast("packetDispatcher", packetDispatcher);
        pipeline.addLast("tailHandler", exceptionHandler);
    }
}
