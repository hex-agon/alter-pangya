package work.fking.pangya.game.net;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import work.fking.pangya.game.GameServer;
import work.fking.pangya.networking.protocol.SimplePacketEncoder;

public class ServerChannelInitializer extends ChannelInitializer<Channel> {

    private final HelloHandler helloHandler;

    private ServerChannelInitializer(GameServer gameServer) {
        this.helloHandler = HelloHandler.create(gameServer);
    }

    public static ServerChannelInitializer create(GameServer gameServer) {
        return new ServerChannelInitializer(gameServer);
    }

    @Override
    protected void initChannel(Channel channel) {
        ChannelPipeline pipeline = channel.pipeline();

        pipeline.addLast("encoder", new SimplePacketEncoder());
        pipeline.addLast(helloHandler);
    }
}
