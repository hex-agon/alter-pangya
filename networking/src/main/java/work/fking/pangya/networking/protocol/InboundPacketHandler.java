package work.fking.pangya.networking.protocol;

import io.netty.channel.Channel;

public interface InboundPacketHandler<P extends InboundPacket> {

    void handle(Channel channel, P packet);
}
