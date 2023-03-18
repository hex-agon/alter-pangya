package work.fking.pangya.login.net;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.fking.pangya.common.Rand;
import work.fking.pangya.login.LoginServer;
import work.fking.pangya.login.packet.outbound.HelloPacket;
import work.fking.pangya.networking.crypt.PangCrypt;
import work.fking.pangya.networking.protocol.ProtocolEncoder;

import java.nio.ByteOrder;

@Sharable
public class HelloHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LogManager.getLogger(HelloHandler.class);

    private final ClientLoginProtocol protocol;
    private final LoginServer loginServer;

    private HelloHandler(ClientLoginProtocol protocol, LoginServer loginServer) {
        this.protocol = protocol;
        this.loginServer = loginServer;
    }

    public static HelloHandler create(ClientLoginProtocol protocol, LoginServer loginServer) {
        return new HelloHandler(protocol, loginServer);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        var channel = ctx.channel();
        int cryptKey = Rand.maxInclusive(PangCrypt.CRYPT_KEY_MAX);
        LOGGER.trace("New connection from {}, selected cryptKey={}", channel.remoteAddress(), cryptKey);

        var player = loginServer.registerPlayer(channel);

        ctx.writeAndFlush(HelloPacket.create(cryptKey));
        var pipeline = channel.pipeline();

        pipeline.remove(this);
        pipeline.replace("encoder", "encoder", ProtocolEncoder.create(cryptKey));
        pipeline.addLast("framer", new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN, 10000, 1, 2, 1, 0, true));
        pipeline.addLast("decoder", GameProtocolDecoder.create(protocol, cryptKey));
        pipeline.addLast("packetDispatcher", ClientLoginPacketDispatcher.create(loginServer, player, protocol.handlers()));
    }
}
