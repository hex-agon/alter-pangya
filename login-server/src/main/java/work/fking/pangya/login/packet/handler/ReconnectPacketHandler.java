package work.fking.pangya.login.packet.handler;

import io.netty.channel.Channel;
import lombok.extern.log4j.Log4j2;
import work.fking.pangya.login.packet.inbound.ReconnectPacket;
import work.fking.pangya.login.packet.outbound.LoginResultPacket;
import work.fking.pangya.login.packet.outbound.LoginResultPacket.Result;
import work.fking.pangya.networking.protocol.InboundPacketHandler;

import javax.inject.Singleton;

@Log4j2
@Singleton
public class ReconnectPacketHandler implements InboundPacketHandler<ReconnectPacket> {

    @Override
    public void handle(Channel channel, ReconnectPacket packet) {
        LOGGER.debug(packet);
        channel.writeAndFlush(LoginResultPacket.error(Result.SERVER_MAINTENANCE));
    }
}
