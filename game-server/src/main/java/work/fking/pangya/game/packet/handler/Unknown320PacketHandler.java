package work.fking.pangya.game.packet.handler;

import io.netty.channel.Channel;
import work.fking.pangya.game.packet.inbound.Unknown320Packet;
import work.fking.pangya.networking.protocol.InboundPacketHandler;

public class Unknown320PacketHandler implements InboundPacketHandler<Unknown320Packet> {

    @Override
    public void handle(Channel channel, Unknown320Packet packet) {

    }
}
