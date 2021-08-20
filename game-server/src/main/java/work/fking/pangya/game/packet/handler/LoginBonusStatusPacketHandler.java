package work.fking.pangya.game.packet.handler;

import io.netty.channel.Channel;
import work.fking.pangya.game.packet.inbound.LoginBonusStatusPacket;
import work.fking.pangya.game.packet.outbound.LoginBonusReplyPacket;
import work.fking.pangya.networking.protocol.InboundPacketHandler;

public class LoginBonusStatusPacketHandler implements InboundPacketHandler<LoginBonusStatusPacket> {

    @Override
    public void handle(Channel channel, LoginBonusStatusPacket packet) {
        channel.writeAndFlush(new LoginBonusReplyPacket());
    }
}
