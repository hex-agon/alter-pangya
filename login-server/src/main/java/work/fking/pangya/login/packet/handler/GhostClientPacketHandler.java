package work.fking.pangya.login.packet.handler;

import io.netty.channel.Channel;
import lombok.extern.log4j.Log4j2;
import work.fking.pangya.login.packet.inbound.GhostClientPacket;
import work.fking.pangya.networking.protocol.InboundPacketHandler;

import javax.inject.Singleton;

@Log4j2
@Singleton
public class GhostClientPacketHandler implements InboundPacketHandler<GhostClientPacket> {

    @Override
    public void handle(Channel channel, GhostClientPacket packet) {
        LOGGER.debug(packet);
    }
}
