package work.fking.pangya.login.packet.handler;

import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.fking.pangya.login.model.LoginRequest;
import work.fking.pangya.login.model.LoginSession;
import work.fking.pangya.login.model.LoginState;
import work.fking.pangya.login.packet.inbound.LoginRequestPacket;
import work.fking.pangya.login.service.LoginService;
import work.fking.pangya.networking.protocol.InboundPacketHandler;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LoginPacketHandler implements InboundPacketHandler<LoginRequestPacket> {

    private static final Logger LOGGER = LogManager.getLogger(LoginPacketHandler.class);

    private final LoginService loginService;

    @Inject
    public LoginPacketHandler(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public void handle(Channel channel, LoginRequestPacket packet) {
        LoginSession session = channel.attr(LoginSession.KEY).get();

        if (session.state() != LoginState.AUTHENTICATING) {
            LOGGER.warn("Unexpected login session state, got={}, expected=AUTHENTICATING", session.state());
            channel.disconnect();
            return;
        }
        LoginRequest loginRequest = LoginRequest.of(channel, packet.username(), packet.passwordMd5());
        loginService.queueLoginRequest(loginRequest);
    }
}
