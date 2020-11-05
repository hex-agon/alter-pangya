package work.fking.pangya.login.packet.handler;

import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.fking.pangya.login.model.LoginSession;
import work.fking.pangya.login.model.LoginState;
import work.fking.pangya.login.model.NicknameCheckRequest;
import work.fking.pangya.login.packet.inbound.CheckNicknamePacket;
import work.fking.pangya.login.service.NicknameService;
import work.fking.pangya.networking.protocol.InboundPacketHandler;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CheckNicknamePacketHandler implements InboundPacketHandler<CheckNicknamePacket> {

    private static final Logger LOGGER = LogManager.getLogger(CheckNicknamePacketHandler.class);

    private final NicknameService nicknameService;

    @Inject
    public CheckNicknamePacketHandler(NicknameService nicknameService) {
        this.nicknameService = nicknameService;
    }

    @Override
    public void handle(Channel channel, CheckNicknamePacket packet) {
        LoginSession session = channel.attr(LoginSession.KEY).get();

        if (session.getState() != LoginState.SELECTING_NICKNAME && session.getState() != LoginState.SELECTED_NICKNAME) {
            LOGGER.warn("Unexpected login session state, got={}, expected=SELECTING_NICKNAME or SELECTED_NICKNAME", session.getState());
            channel.disconnect();
            return;
        }
        nicknameService.queueNicknameCheckRequest(NicknameCheckRequest.of(channel, packet.nickname()));
    }
}
