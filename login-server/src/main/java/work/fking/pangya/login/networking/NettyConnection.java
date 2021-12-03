package work.fking.pangya.login.networking;

import io.netty.channel.Channel;
import work.fking.pangya.networking.protocol.OutboundPacket;

public class NettyConnection implements ClientConnection {

    private final Channel channel;

    public NettyConnection(Channel channel) {
        this.channel = channel;
    }

    @Override
    public boolean isAlive() {
        return channel.isActive();
    }

    @Override
    public void disconnect() {
        channel.disconnect();
    }

    @Override
    public void write(OutboundPacket packet) {
        channel.write(packet);
    }

    @Override
    public void flush() {
        channel.flush();
    }

    @Override
    public void writeAndFlush(OutboundPacket packet) {
        channel.writeAndFlush(packet);
    }
}
