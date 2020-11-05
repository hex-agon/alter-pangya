package work.fking.pangya.game.packet.handler;

import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.fking.pangya.game.packet.inbound.HandoverPacket;
import work.fking.pangya.networking.protocol.InboundPacketHandler;

import javax.inject.Singleton;

@Singleton
public class HandoverPacketHandler implements InboundPacketHandler<HandoverPacket> {

    private static final Logger LOGGER = LogManager.getLogger(HandoverPacketHandler.class);

    @Override
    public void handle(Channel channel, HandoverPacket packet) {
        LOGGER.debug(packet);
        channel.disconnect();
    }
}
