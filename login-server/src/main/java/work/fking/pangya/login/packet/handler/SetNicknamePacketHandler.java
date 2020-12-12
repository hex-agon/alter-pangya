package work.fking.pangya.login.packet.handler;

import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.fking.pangya.login.model.LoginSession;
import work.fking.pangya.login.model.LoginState;
import work.fking.pangya.login.model.NicknameSetRequest;
import work.fking.pangya.login.packet.inbound.SetNicknamePacket;
import work.fking.pangya.login.service.LoginService;
import work.fking.pangya.login.service.NicknameService;
import work.fking.pangya.networking.protocol.InboundPacketHandler;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SetNicknamePacketHandler implements InboundPacketHandler<SetNicknamePacket> {

    private static final Logger LOGGER = LogManager.getLogger(SetNicknamePacketHandler.class);

    private final NicknameService nicknameService;
    private final LoginService loginService;

    @Inject
    public SetNicknamePacketHandler(NicknameService nicknameService, LoginService loginService) {
        this.nicknameService = nicknameService;
        this.loginService = loginService;
    }

    @Override
    public void handle(Channel channel, SetNicknamePacket packet) {
        LoginSession session = channel.attr(LoginSession.KEY).get();

        if (session.state() != LoginState.SELECTED_NICKNAME) {
            LOGGER.warn("Unexpected login session state, got={}, expected=SELECTED_NICKNAME", session.state());
            channel.disconnect();
            return;
        }
        NicknameSetRequest request = NicknameSetRequest.of(channel, packet.nickname(), result -> loginService.resumeLoginFlow(session));
        nicknameService.queueNicknameCheckSet(request);
    }
}
