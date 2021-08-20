package work.fking.pangya.game.packet.handler;

import io.netty.channel.Channel;
import work.fking.pangya.game.packet.inbound.MyRoomOpenPacket;
import work.fking.pangya.game.packet.inbound.MyRoomOpenedPacket;
import work.fking.pangya.networking.protocol.InboundPacketHandler;

public class MyRoomOpenedPacketHandler implements InboundPacketHandler<MyRoomOpenedPacket> {

    @Override
    public void handle(Channel channel, MyRoomOpenedPacket packet) {

    }
}
