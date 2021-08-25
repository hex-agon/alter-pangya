package work.fking.pangya.login.packet.handler;

import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.fking.pangya.login.model.LoginSession;
import work.fking.pangya.login.model.LoginState;
import work.fking.pangya.login.model.NewProfileRequest;
import work.fking.pangya.login.packet.inbound.SelectCharacterPacket;
import work.fking.pangya.login.service.LoginService;
import work.fking.pangya.login.service.ProfileService;
import work.fking.pangya.networking.protocol.InboundPacketHandler;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SelectCharacterPacketHandler implements InboundPacketHandler<SelectCharacterPacket> {

    private static final Logger LOGGER = LogManager.getLogger(SelectCharacterPacketHandler.class);

    private final LoginService loginService;
    private final ProfileService profileService;

    @Inject
    public SelectCharacterPacketHandler(LoginService loginService, ProfileService profileService) {
        this.loginService = loginService;
        this.profileService = profileService;
    }

    @Override
    public void handle(Channel channel, SelectCharacterPacket packet) {
        LoginSession session = channel.attr(LoginSession.KEY).get();

        if (session.state() != LoginState.SELECTING_CHARACTER) {
            LOGGER.warn("Unexpected login session state, got={}, expected=SELECTING_CHARACTER", session.state());
            channel.disconnect();
            return;
        }
        profileService.queueNewProfileRequest(new NewProfileRequest(channel, packet.characterId(), packet.hairColor(), result -> loginService.resumeLoginFlow(session)));
    }
}
