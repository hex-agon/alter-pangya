package work.fking.pangya.game.packet.handler;

import io.netty.channel.Channel;
import work.fking.pangya.game.packet.inbound.SelectChannelPacket;
import work.fking.pangya.game.packet.outbound.SelectChannelResultPacket;
import work.fking.pangya.networking.protocol.InboundPacketHandler;

public class SelectChannelPacketHandler implements InboundPacketHandler<SelectChannelPacket> {

    @Override
    public void handle(Channel channel, SelectChannelPacket packet) {
        channel.writeAndFlush(new SelectChannelResultPacket());
    }
}
