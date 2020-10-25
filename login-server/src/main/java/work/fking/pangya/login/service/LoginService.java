package work.fking.pangya.login.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Verifyer;
import io.netty.channel.Channel;
import lombok.extern.log4j.Log4j2;
import work.fking.pangya.login.model.LoginRequest;
import work.fking.pangya.login.model.LoginSession;
import work.fking.pangya.login.model.LoginState;
import work.fking.pangya.login.model.PlayerAccount;
import work.fking.pangya.login.packet.outbound.LoginKeyPacket;
import work.fking.pangya.login.packet.outbound.LoginResultPacket;
import work.fking.pangya.login.packet.outbound.LoginResultPacket.Result;
import work.fking.pangya.login.packet.outbound.ServerListPacket;
import work.fking.pangya.login.repository.PlayerAccountRepository;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;

@Log4j2
@Singleton
public class LoginService {

    private static final Verifyer BCRYPT_VERIFIER = BCrypt.verifyer();

    private final PlayerAccountRepository playerRepository;
    private final ExecutorService executorService;

    @Inject
    public LoginService(PlayerAccountRepository playerRepository, @Named("shared") ExecutorService executorService) {
        this.playerRepository = playerRepository;
        this.executorService = executorService;
    }

    public void queueLoginRequest(LoginRequest loginRequest) {
        executorService.submit(() -> authenticate(loginRequest));
    }

    public void resumeLoginFlow(LoginSession loginSession) {
        LOGGER.debug("resumeLoginFlow enter state={}", loginSession.getState());
        if (loginSession.getState() == LoginState.AUTHENTICATING) {
            throw new IllegalStateException("Cannot resume login flow when session state is AUTHENTICATING");
        }
        checkSetupTasks(loginSession);
        LOGGER.debug("resumeLoginFlow checkSetupTasks state={}", loginSession.getState());

        switch (loginSession.getState()) {

            case AUTHENTICATED -> proceedToLoggedIn(loginSession);
            case SELECTING_NICKNAME -> proceedToNicknameCreation(loginSession);
            case SELECTING_CHARACTER -> proceedToCharacterSelection(loginSession);
            default -> unexpectedState(loginSession);
        }
    }

    // check if we need to set a nickname or select a character
    private void checkSetupTasks(LoginSession session) {
        PlayerAccount playerAccount = session.getPlayerAccount();

        session.updateState(LoginState.SELECTING_CHARACTER);

        if (playerAccount.nickname() == null) {
            session.updateState(LoginState.SELECTING_NICKNAME);
        } else {
            session.updateState(LoginState.AUTHENTICATED);
        }
    }

    private void authenticate(LoginRequest loginRequest) {
        LOGGER.debug("Processing login request for username={} from ip={}", loginRequest::username, () -> loginRequest.channel().remoteAddress());
        LoginSession session = loginRequest.channel().attr(LoginSession.KEY).get();
        PlayerAccount playerAccount = loadAndCheckPassword(loginRequest);

        if (playerAccount == null) {
            loginRequest.channel().writeAndFlush(LoginResultPacket.error(Result.INCORRECT_USERNAME_PASSWORD).build());
            return;
        }

        session.setPlayerAccount(playerAccount);
        session.updateState(LoginState.AUTHENTICATED);

        switch (playerAccount.status()) {
            case DISABLED -> replyAccountBanned(session);
            case SUSPENDED -> checkAccountSuspended(session);
            default -> resumeLoginFlow(session);
        }
    }

    private PlayerAccount loadAndCheckPassword(LoginRequest loginRequest) {
        PlayerAccount playerAccount = playerRepository.findByUsername(loginRequest.username());

        if (playerAccount == null) {
            return null;
        }
        BCrypt.Result bCryptResult = BCRYPT_VERIFIER.verify(loginRequest.passwordMd5(), playerAccount.passwordHash());

        if (!bCryptResult.verified) {
            return null;
        }
        loginRequest.clearPasswordMd5();
        playerAccount.clearPasswordHash();
        bCryptResult.details.wipe();

        return playerAccount;
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

    private void replyAccountBanned(LoginSession loginSession) {
        loginSession.getChannel().writeAndFlush(LoginResultPacket.error(Result.BANNED).build());
    }

    private void checkAccountSuspended(LoginSession loginSession) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime suspensionLiftDateTime = loginSession.getPlayerAccount().suspensionLiftTimestamp();

        if (now.isBefore(suspensionLiftDateTime)) {
            int hours = (int) Duration.between(now, suspensionLiftDateTime).toHours();

            if (hours < 1) {
                hours = 1;
            }
            LoginResultPacket build = LoginResultPacket.error(Result.ACCOUNT_SUSPENDED)
                                                       .suspensionTime(hours)
                                                       .build();
            loginSession.getChannel().writeAndFlush(build);
        }
    }

    private void unexpectedState(LoginSession loginSession) {
        LOGGER.warn("Unexpected login session state, state={}", loginSession.getState());
        loginSession.getChannel().disconnect();
    }
}
