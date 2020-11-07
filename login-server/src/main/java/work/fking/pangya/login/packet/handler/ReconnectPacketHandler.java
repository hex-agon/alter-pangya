package work.fking.pangya.login.packet.handler;

import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.fking.pangya.login.packet.inbound.ReconnectPacket;
import work.fking.pangya.login.packet.outbound.LoginResultPacket;
import work.fking.pangya.login.packet.outbound.LoginResultPacket.Result;
import work.fking.pangya.networking.protocol.InboundPacketHandler;

import javax.inject.Singleton;

@Singleton
public class ReconnectPacketHandler implements InboundPacketHandler<ReconnectPacket> {

    private static final Logger LOGGER = LogManager.getLogger(ReconnectPacketHandler.class);

    @Override
    public void handle(Channel channel, ReconnectPacket packet) {
        LOGGER.debug(packet);
        channel.writeAndFlush(LoginResultPacket.error(Result.BANNED).build());
    }
}
