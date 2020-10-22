package work.fking.pangya;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import lombok.extern.log4j.Log4j2;
import work.fking.pangya.networking.protocol.InboundPacketDispatcher;
import work.fking.pangya.networking.protocol.Packet;
import work.fking.pangya.networking.protocol.Protocol;
import work.fking.pangya.networking.protocol.SimplePacketEncoder;

@Log4j2
public class LoginServerChannelInitializer extends ChannelInitializer<Channel> {

    private final HelloHandler helloHandler;

    private LoginServerChannelInitializer(Protocol protocol, InboundPacketDispatcher packetDispatcher) {
        this.helloHandler = HelloHandler.create(protocol, packetDispatcher);
    }

    public static LoginServerChannelInitializer create(Protocol protocol, InboundPacketDispatcher packetDispatcher) {
        return new LoginServerChannelInitializer(protocol, packetDispatcher);
    }

    @Override
    protected void initChannel(Channel channel) {
        ChannelPipeline pipeline = channel.pipeline();

        pipeline.addLast("encoder", new SimplePacketEncoder());
        pipeline.addLast(helloHandler);
    }
}
