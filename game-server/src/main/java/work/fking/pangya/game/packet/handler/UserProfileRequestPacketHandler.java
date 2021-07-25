package work.fking.pangya.game.packet.handler;

import io.netty.channel.Channel;
import work.fking.pangya.game.packet.inbound.UserProfileRequestPacket;
import work.fking.pangya.networking.protocol.InboundPacketHandler;

public class UserProfileRequestPacketHandler implements InboundPacketHandler<UserProfileRequestPacket> {

    @Override
    public void handle(Channel channel, UserProfileRequestPacket packet) {

    }
}
