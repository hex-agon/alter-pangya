package work.fking.pangya.login.packet.handler;

import io.netty.channel.Channel;
import work.fking.pangya.login.packet.inbound.SelectServerPacket;
import work.fking.pangya.login.packet.outbound.LoginReplies;
import work.fking.pangya.networking.protocol.InboundPacketHandler;

import javax.inject.Singleton;

@Singleton
public class SelectServerPacketHandler implements InboundPacketHandler<SelectServerPacket> {

    @Override
    public void handle(Channel channel, SelectServerPacket packet) {
        channel.writeAndFlush(LoginReplies.sessionKey("sessionKey"));
    }
}
