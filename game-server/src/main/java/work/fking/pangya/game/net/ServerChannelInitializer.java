package work.fking.pangya.game.net;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import work.fking.pangya.game.GameServer;
import work.fking.pangya.networking.protocol.SimplePacketEncoder;

public class ServerChannelInitializer extends ChannelInitializer<Channel> {

    private final HelloHandler helloHandler;

    private ServerChannelInitializer(ClientGameProtocol protocol, GameServer gameServer) {
        this.helloHandler = HelloHandler.create(protocol, gameServer);
    }

    public static ServerChannelInitializer create(ClientGameProtocol protocol, GameServer gameServer) {
        return new ServerChannelInitializer(protocol, gameServer);
    }

    @Override
    protected void initChannel(Channel channel) {
        ChannelPipeline pipeline = channel.pipeline();

        pipeline.addLast("encoder", new SimplePacketEncoder());
        pipeline.addLast(helloHandler);
    }
}
