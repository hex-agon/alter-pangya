package work.fking.pangya.login.packet.handler;

import io.netty.channel.Channel;
import lombok.extern.log4j.Log4j2;
import work.fking.pangya.login.model.LoginRequest;
import work.fking.pangya.login.model.LoginSession;
import work.fking.pangya.login.model.LoginState;
import work.fking.pangya.login.packet.inbound.LoginRequestPacket;
import work.fking.pangya.login.service.LoginService;
import work.fking.pangya.networking.protocol.InboundPacketHandler;

import javax.inject.Inject;
import javax.inject.Singleton;

@Log4j2
@Singleton
public class LoginPacketHandler implements InboundPacketHandler<LoginRequestPacket> {

    private final LoginService loginService;

    @Inject
    public LoginPacketHandler(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public void handle(Channel channel, LoginRequestPacket packet) {
        LoginSession session = channel.attr(LoginSession.KEY).get();

        if (session.getState() != LoginState.AUTHENTICATING) {
            LOGGER.warn("Unexpected login session state, got={}, expected=AUTHENTICATING", session.getState());
            channel.disconnect();
            return;
        }
        LoginRequest loginRequest = LoginRequest.of(channel, packet.username(), packet.passwordMd5());
        loginService.queueLoginRequest(loginRequest);
    }
}
