package work.fking.pangya.login.packet.handler;

import io.netty.channel.Channel;
import lombok.extern.log4j.Log4j2;
import work.fking.pangya.login.model.LoginSession;
import work.fking.pangya.login.model.LoginState;
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
        LoginSession session = channel.attr(LoginSession.KEY).get();

        if (session.getState() != LoginState.AUTHENTICATING) {
            LOGGER.warn("Unexpected login session state, got={}, expected=AUTHENTICATING", session.getState());
            channel.disconnect();
            return;
        }
        LoginResultPacket loginResultPacket = LoginResultPacket.error(Result.REQUEST_NICKNAME)
                                                               .build();
        session.updateState(LoginState.SELECTING_NICKNAME);

        channel.write(loginResultPacket);
        channel.flush();
    }
}
