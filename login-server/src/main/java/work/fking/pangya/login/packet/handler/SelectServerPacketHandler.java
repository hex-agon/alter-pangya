package work.fking.pangya.login.packet.handler;

import io.netty.channel.Channel;
import lombok.extern.log4j.Log4j2;
import work.fking.pangya.login.packet.inbound.SelectServerPacket;
import work.fking.pangya.login.packet.outbound.SessionKeyPacket;
import work.fking.pangya.networking.protocol.InboundPacketHandler;

import javax.inject.Singleton;

@Log4j2
@Singleton
public class SelectServerPacketHandler implements InboundPacketHandler<SelectServerPacket> {

    @Override
    public void handle(Channel channel, SelectServerPacket packet) {
        LOGGER.debug(packet);
        channel.write(SessionKeyPacket.create("sessionKey"));
        channel.flush();
    }
}
