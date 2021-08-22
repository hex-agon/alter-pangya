package work.fking.pangya.login.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Verifyer;
import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.fking.pangya.discovery.DiscoveryClient;
import work.fking.pangya.discovery.ServerType;
import work.fking.pangya.login.LoginServer;
import work.fking.pangya.login.model.LoginRequest;
import work.fking.pangya.login.model.LoginSession;
import work.fking.pangya.login.model.LoginState;
import work.fking.pangya.login.model.PlayerAccount;
import work.fking.pangya.login.packet.outbound.LoginReplies;
import work.fking.pangya.login.packet.outbound.LoginReplies.Error;
import work.fking.pangya.login.packet.outbound.ServerListReplies;
import work.fking.pangya.login.repository.PlayerAccountRepository;
import work.fking.pangya.login.repository.PlayerProfileRepository;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;

@Singleton
public class LoginService {

    private static final Logger LOGGER = LogManager.getLogger(LoginService.class);

    private static final Verifyer BCRYPT_VERIFIER = BCrypt.verifyer();

    private final PlayerAccountRepository playerRepository;
    private final PlayerProfileRepository profileRepository;
    private final LoginServer registry;
    private final DiscoveryClient discoveryClient;
    private final ExecutorService executorService;

    @Inject
    public LoginService(
            PlayerAccountRepository playerRepository,
            PlayerProfileRepository profileRepository,
            LoginServer registry,
            DiscoveryClient discoveryClient,
            @Named("shared") ExecutorService executorService
    ) {
        this.playerRepository = playerRepository;
        this.profileRepository = profileRepository;
        this.registry = registry;
        this.discoveryClient = discoveryClient;
        this.executorService = executorService;
    }

    public void queueLoginRequest(LoginRequest loginRequest) {
        executorService.execute(() -> authenticate(loginRequest));
    }

    public void resumeLoginFlow(LoginSession loginSession) {
        LOGGER.debug("resumeLoginFlow enter state={}", loginSession.state());
        if (loginSession.state() == LoginState.AUTHENTICATING) {
            throw new IllegalStateException("Cannot resume login flow when session state is AUTHENTICATING");
        }
        checkSetupTasks(loginSession);
        LOGGER.debug("resumeLoginFlow checkSetupTasks state={}", loginSession.state());

        switch (loginSession.state()) {

            case AUTHENTICATED -> proceedToLoggedIn(loginSession);
            case SELECTING_NICKNAME -> proceedToNicknameCreation(loginSession);
            case SELECTING_CHARACTER -> proceedToCharacterSelection(loginSession);
            default -> unexpectedState(loginSession);
        }
    }

    private void checkSetupTasks(LoginSession session) {
        PlayerAccount playerAccount = session.playerAccount();

        if (playerAccount.nickname() == null) {
            session.updateState(LoginState.SELECTING_NICKNAME);
        } else if (!profileRepository.hasActiveCharacter(playerAccount.id())) {
            session.updateState(LoginState.SELECTING_CHARACTER);
        } else {
            session.updateState(LoginState.AUTHENTICATED);
        }
    }

    private void authenticate(LoginRequest loginRequest) {
        LOGGER.debug("Processing login request for username={} from ip={}", loginRequest::username, () -> loginRequest.channel().remoteAddress());
        LoginSession session = loginRequest.channel().attr(LoginSession.KEY).get();
        PlayerAccount playerAccount = loadAndCheckPassword(loginRequest);

        if (playerAccount == null) {
            loginRequest.channel().writeAndFlush(LoginReplies.error(Error.INCORRECT_USERNAME_PASSWORD));
            return;
        }

        session.setPlayerAccount(playerAccount);
        session.updateState(LoginState.AUTHENTICATED);
        registry.register(session);

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
        PlayerAccount playerAccount = session.playerAccount();
        session.updateState(LoginState.LOGGED_IN);

        var gameServers = discoveryClient.instances(ServerType.GAME);
        var socialServers = discoveryClient.instances(ServerType.SOCIAL);

        Channel channel = session.channel();
        channel.write(LoginReplies.loginKey("loginKey"));
        channel.write(LoginReplies.chatMacros());
        channel.write(LoginReplies.success(playerAccount.id(), playerAccount.username(), playerAccount.nickname()));
        channel.write(ServerListReplies.gameServers(gameServers));
        channel.write(ServerListReplies.socialServers(socialServers));
        channel.flush();
    }

    private void proceedToCharacterSelection(LoginSession session) {
        session.channel().writeAndFlush(LoginReplies.selectCharacter());
    }

    private void proceedToNicknameCreation(LoginSession session) {
        session.channel().writeAndFlush(LoginReplies.createNickname());
    }

    private void replyAccountBanned(LoginSession loginSession) {
        loginSession.channel().writeAndFlush(LoginReplies.error(Error.BANNED));
    }

    private void checkAccountSuspended(LoginSession loginSession) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime suspensionLiftDateTime = loginSession.playerAccount().suspensionLiftTimestamp();

        if (now.isBefore(suspensionLiftDateTime)) {
            int hours = Duration.between(now, suspensionLiftDateTime).toHoursPart();

            if (hours < 1) {
                hours = 1;
            }
            loginSession.channel().writeAndFlush(LoginReplies.accountSuspended(hours));
        }
    }

    private void unexpectedState(LoginSession loginSession) {
        LOGGER.warn("Unexpected login session state, state={}", loginSession.state());
        loginSession.channel().disconnect();
    }
}
