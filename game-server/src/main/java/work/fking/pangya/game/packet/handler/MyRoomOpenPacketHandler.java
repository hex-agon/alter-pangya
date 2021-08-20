package work.fking.pangya.game.packet.handler;

import io.netty.channel.Channel;
import work.fking.pangya.game.packet.inbound.MyRoomOpenPacket;
import work.fking.pangya.networking.protocol.InboundPacketHandler;

public class MyRoomOpenPacketHandler implements InboundPacketHandler<MyRoomOpenPacket> {

    @Override
    public void handle(Channel channel, MyRoomOpenPacket packet) {

    }
}
