package work.fking.pangya.login.packet.handler;

import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.fking.pangya.login.packet.inbound.SelectServerPacket;
import work.fking.pangya.login.packet.outbound.SessionKeyPacket;
import work.fking.pangya.networking.protocol.InboundPacketHandler;

import javax.inject.Singleton;

@Singleton
public class SelectServerPacketHandler implements InboundPacketHandler<SelectServerPacket> {

    @Override
    public void handle(Channel channel, SelectServerPacket packet) {
        channel.write(SessionKeyPacket.create("sessionKey"));
        channel.flush();
    }
}
