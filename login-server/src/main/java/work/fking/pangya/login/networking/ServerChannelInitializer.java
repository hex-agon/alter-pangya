package work.fking.pangya.login.networking;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import work.fking.pangya.networking.protocol.InboundPacketDispatcher;
import work.fking.pangya.networking.protocol.Protocol;
import work.fking.pangya.networking.protocol.SimplePacketEncoder;

public class ServerChannelInitializer extends ChannelInitializer<Channel> {

    private final HelloHandler helloHandler;

    private ServerChannelInitializer(Protocol protocol, InboundPacketDispatcher packetDispatcher, ExceptionHandler exceptionHandler) {
        this.helloHandler = HelloHandler.create(protocol, packetDispatcher, exceptionHandler);
    }

    public static ServerChannelInitializer create(Protocol protocol, InboundPacketDispatcher packetDispatcher, ExceptionHandler exceptionHandler) {
        return new ServerChannelInitializer(protocol, packetDispatcher, exceptionHandler);
    }

    @Override
    protected void initChannel(Channel channel) {
        ChannelPipeline pipeline = channel.pipeline();

        pipeline.addLast("encoder", new SimplePacketEncoder());
        pipeline.addLast(helloHandler);
    }
}
