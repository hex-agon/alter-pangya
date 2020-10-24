package work.fking.pangya.login.service;

import io.netty.channel.Channel;
import lombok.extern.log4j.Log4j2;
import work.fking.pangya.login.model.LoginRequest;
import work.fking.pangya.login.model.LoginSession;
import work.fking.pangya.login.model.LoginState;
import work.fking.pangya.login.packet.outbound.LoginKeyPacket;
import work.fking.pangya.login.packet.outbound.LoginResultPacket;
import work.fking.pangya.login.packet.outbound.LoginResultPacket.Result;
import work.fking.pangya.login.packet.outbound.ServerListPacket;

import javax.inject.Singleton;

@Log4j2
@Singleton
public class LoginService {

    public void queueLoginRequest(LoginRequest loginRequest) {
        //TODO: Async processing
        authenticate(loginRequest);
    }

    public void resumeLoginFlow(LoginSession loginSession) {

        if (loginSession.getState() == LoginState.AUTHENTICATING) {
            throw new IllegalStateException("Cannot resume login flow when session state is AUTHENTICATING");
        }
        checkSetupTasks(loginSession);

        switch (loginSession.getState()) {

            case AUTHENTICATED -> proceedToLoggedIn(loginSession);
            case SELECTING_NICKNAME -> proceedToNicknameCreation(loginSession);
            case SELECTING_CHARACTER -> proceedToCharacterSelection(loginSession);
            default -> unexpectedState(loginSession);
        }
    }

    // check if we need to set a nickname or select a character
    private void checkSetupTasks(LoginSession session) {
        session.updateState(LoginState.SELECTING_CHARACTER);
        session.updateState(LoginState.SELECTING_NICKNAME);

        session.updateState(LoginState.AUTHENTICATED);
    }

    private void authenticate(LoginRequest loginRequest) {
        LoginSession session = loginRequest.channel().attr(LoginSession.KEY).get();
        // TODO: Authentication
        loginRequest.clearPasswordMd5();

        session.updateState(LoginState.AUTHENTICATED);
        resumeLoginFlow(session);
    }

    private void proceedToLoggedIn(LoginSession session) {
        LoginResultPacket loginResultPacket = LoginResultPacket.success()
                                                               .userId(1)
                                                               .username("hello")
                                                               .nickname("nickname")
                                                               .build();

        session.updateState(LoginState.LOGGED_IN);

        Channel channel = session.getChannel();
        channel.write(loginResultPacket);
        channel.write(new LoginKeyPacket());
        channel.write(new ServerListPacket());
        channel.flush();
    }

    private void proceedToCharacterSelection(LoginSession session) {
        LoginResultPacket loginResultPacket = LoginResultPacket.error(Result.SELECT_CHARACTER).build();

        session.updateState(LoginState.SELECTING_CHARACTER);
        session.getChannel().writeAndFlush(loginResultPacket);
    }

    private void proceedToNicknameCreation(LoginSession session) {
        LoginResultPacket loginResultPacket = LoginResultPacket.error(Result.CREATE_NICKNAME).build();

        session.updateState(LoginState.SELECTING_NICKNAME);
        session.getChannel().writeAndFlush(loginResultPacket);
    }

    private void unexpectedState(LoginSession loginSession) {
        LOGGER.warn("Unexpected login session state, state={}", loginSession.getState());
        loginSession.getChannel().disconnect();
    }
}
