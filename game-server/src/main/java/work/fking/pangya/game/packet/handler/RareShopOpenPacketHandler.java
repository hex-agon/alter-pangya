package work.fking.pangya.game.packet.handler;

import io.netty.channel.Channel;
import work.fking.pangya.game.packet.inbound.RareShopOpenPacket;
import work.fking.pangya.game.packet.outbound.RareShopOpenResponsePacket;
import work.fking.pangya.networking.protocol.InboundPacketHandler;

public class RareShopOpenPacketHandler implements InboundPacketHandler<RareShopOpenPacket> {

    @Override
    public void handle(Channel channel, RareShopOpenPacket packet) {
        channel.writeAndFlush(new RareShopOpenResponsePacket());
    }
}
