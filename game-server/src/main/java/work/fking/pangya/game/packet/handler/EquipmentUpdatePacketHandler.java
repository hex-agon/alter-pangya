package work.fking.pangya.game.packet.handler;

import io.netty.channel.Channel;
import work.fking.pangya.game.packet.inbound.EquipmentUpdatePacket;
import work.fking.pangya.networking.protocol.InboundPacketHandler;

public class EquipmentUpdatePacketHandler implements InboundPacketHandler<EquipmentUpdatePacket> {

    @Override
    public void handle(Channel channel, EquipmentUpdatePacket packet) {

    }
}
