package work.fking.pangya.login.packet.handler;

import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.fking.pangya.login.packet.inbound.GhostClientPacket;
import work.fking.pangya.networking.protocol.InboundPacketHandler;

import javax.inject.Singleton;

@Singleton
public class GhostClientPacketHandler implements InboundPacketHandler<GhostClientPacket> {

    private static final Logger LOGGER = LogManager.getLogger(GhostClientPacketHandler.class);

    @Override
    public void handle(Channel channel, GhostClientPacket packet) {
        LOGGER.debug(packet);
    }
}
