package work.fking.pangya.login.packet.handler;

import io.netty.channel.Channel;
import lombok.extern.log4j.Log4j2;
import work.fking.pangya.login.model.LoginSession;
import work.fking.pangya.login.model.LoginState;
import work.fking.pangya.login.packet.inbound.SelectCharacterPacket;
import work.fking.pangya.login.packet.outbound.ConfirmCharacterSelectionPacket;
import work.fking.pangya.login.service.LoginService;
import work.fking.pangya.networking.protocol.InboundPacketHandler;

import javax.inject.Inject;
import javax.inject.Singleton;

@Log4j2
@Singleton
public class SelectCharacterPacketHandler implements InboundPacketHandler<SelectCharacterPacket> {

    private final LoginService loginService;

    @Inject
    public SelectCharacterPacketHandler(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public void handle(Channel channel, SelectCharacterPacket packet) {
        LoginSession session = channel.attr(LoginSession.KEY).get();

        if (session.getState() != LoginState.SELECTING_CHARACTER) {
            LOGGER.warn("Unexpected login session state, got={}, expected=SELECTING_CHARACTER", session.getState());
            channel.disconnect();
            return;
        }
        channel.write(ConfirmCharacterSelectionPacket.instance());
        loginService.resumeLoginFlow(session);
    }
}
