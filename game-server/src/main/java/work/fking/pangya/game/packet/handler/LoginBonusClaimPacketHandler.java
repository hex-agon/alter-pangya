package work.fking.pangya.game.packet.handler;

import io.netty.channel.Channel;
import work.fking.pangya.game.packet.inbound.LoginBonusClaimPacket;
import work.fking.pangya.game.packet.inbound.LoginBonusStatusPacket;
import work.fking.pangya.game.packet.outbound.LoginBonusClaimReplyPacket;
import work.fking.pangya.game.packet.outbound.LoginBonusReplyPacket;
import work.fking.pangya.networking.protocol.InboundPacketHandler;

public class LoginBonusClaimPacketHandler implements InboundPacketHandler<LoginBonusClaimPacket> {

    @Override
    public void handle(Channel channel, LoginBonusClaimPacket packet) {
        channel.writeAndFlush(new LoginBonusClaimReplyPacket());
    }
}
