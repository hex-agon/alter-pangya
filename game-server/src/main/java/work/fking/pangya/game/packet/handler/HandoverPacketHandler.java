package work.fking.pangya.game.packet.handler;

import io.netty.channel.Channel;
import lombok.extern.log4j.Log4j2;
import work.fking.pangya.game.packet.inbound.HandoverPacket;
import work.fking.pangya.networking.protocol.InboundPacketHandler;

import javax.inject.Singleton;

@Log4j2
@Singleton
public class HandoverPacketHandler implements InboundPacketHandler<HandoverPacket> {

    @Override
    public void handle(Channel channel, HandoverPacket packet) {
        LOGGER.debug(packet);
        channel.disconnect();
    }
}
