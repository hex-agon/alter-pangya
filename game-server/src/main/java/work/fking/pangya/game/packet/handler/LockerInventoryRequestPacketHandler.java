package work.fking.pangya.game.packet.handler;

import io.netty.channel.Channel;
import work.fking.pangya.game.packet.inbound.LockerInventoryRequestPacket;
import work.fking.pangya.networking.protocol.InboundPacketHandler;

public class LockerInventoryRequestPacketHandler implements InboundPacketHandler<LockerInventoryRequestPacket> {

    @Override
    public void handle(Channel channel, LockerInventoryRequestPacket packet) {

    }
}
