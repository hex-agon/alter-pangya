package work.fking.pangya.login.packet.handler;

import io.netty.channel.Channel;
import lombok.extern.log4j.Log4j2;
import work.fking.pangya.login.packet.inbound.LoginRequestPacket;
import work.fking.pangya.login.packet.outbound.LoginResultPacket;
import work.fking.pangya.login.packet.outbound.LoginResultPacket.Result;
import work.fking.pangya.networking.protocol.InboundPacketHandler;

import javax.inject.Singleton;

@Log4j2
@Singleton
public class LoginPacketHandler implements InboundPacketHandler<LoginRequestPacket> {

    @Override
    public void handle(Channel channel, LoginRequestPacket packet) {
        LOGGER.debug(packet);
        LoginResultPacket loginResultPacket = LoginResultPacket.error(Result.SELECT_CHARACTER)
                                                               .build();

        channel.write(loginResultPacket);
        channel.flush();
    }
}
