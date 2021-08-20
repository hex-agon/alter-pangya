package work.fking.pangya.game.packet.handler;

import io.netty.channel.Channel;
import work.fking.pangya.game.packet.inbound.CreateRoomPacket;
import work.fking.pangya.networking.protocol.InboundPacketHandler;

public class CreateRoomPacketHandler implements InboundPacketHandler<CreateRoomPacket> {

    @Override
    public void handle(Channel channel, CreateRoomPacket packet) {
        System.out.println(packet);
    }
}
