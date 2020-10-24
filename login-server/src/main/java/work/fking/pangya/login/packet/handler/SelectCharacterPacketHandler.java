package work.fking.pangya.login.packet.handler;

import io.netty.channel.Channel;
import lombok.extern.log4j.Log4j2;
import work.fking.pangya.login.packet.inbound.SelectCharacterPacket;
import work.fking.pangya.networking.protocol.InboundPacketHandler;

import javax.inject.Singleton;

@Log4j2
@Singleton
public class SelectCharacterPacketHandler implements InboundPacketHandler<SelectCharacterPacket> {

    @Override
    public void handle(Channel channel, SelectCharacterPacket packet) {
        LOGGER.debug(packet);
    }
}
