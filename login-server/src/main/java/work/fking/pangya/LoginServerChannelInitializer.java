package work.fking.pangya;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import lombok.extern.log4j.Log4j2;
import work.fking.pangya.networking.protocol.Protocol;
import work.fking.pangya.networking.protocol.ProtocolEncoder;

@Log4j2
public class LoginServerChannelInitializer extends ChannelInitializer<Channel> {

    private final HelloHandler helloHandler;

    private LoginServerChannelInitializer(Protocol protocol) {
        this.helloHandler = HelloHandler.create(protocol);
    }

    public static LoginServerChannelInitializer create(Protocol protocol) {
        return new LoginServerChannelInitializer(protocol);
    }

    @Override
    protected void initChannel(Channel channel) {
        ChannelPipeline pipeline = channel.pipeline();

        pipeline.addLast(new ProtocolEncoder());
        pipeline.addLast(helloHandler);
    }
}
