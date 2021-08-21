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
import work.fking.pangya.login.packet.outbound.ChatMacrosPacket;
import work.fking.pangya.login.packet.outbound.GameServerListPacket;
import work.fking.pangya.login.packet.outbound.LoginKeyPacket;
import work.fking.pangya.login.packet.outbound.LoginResultPacket;
import work.fking.pangya.login.packet.outbound.LoginResultPacket.Result;
import work.fking.pangya.login.packet.outbound.MessageServerListPacket;
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
            loginRequest.channel().writeAndFlush(LoginResultPacket.error(Result.INCORRECT_USERNAME_PASSWORD).build());
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
        LoginResultPacket loginResultPacket = LoginResultPacket.success()
                                                               .userId(playerAccount.id())
                                                               .username(playerAccount.username())
                                                               .nickname(playerAccount.nickname())
                                                               .build();

        session.updateState(LoginState.LOGGED_IN);

        var gameServers = discoveryClient.instances(ServerType.GAME);

        Channel channel = session.channel();
        channel.writeAndFlush(LoginKeyPacket.create("7430F52"));
        channel.write(new ChatMacrosPacket());
        channel.write(loginResultPacket);
        channel.write(GameServerListPacket.create(gameServers));
        channel.write(new MessageServerListPacket());
        channel.flush();
    }

    private void proceedToCharacterSelection(LoginSession session) {
        LoginResultPacket loginResultPacket = LoginResultPacket.error(Result.SELECT_CHARACTER).build();

        session.channel().writeAndFlush(loginResultPacket);
    }

    private void proceedToNicknameCreation(LoginSession session) {
        LoginResultPacket loginResultPacket = LoginResultPacket.error(Result.CREATE_NICKNAME).build();

        session.channel().writeAndFlush(loginResultPacket);
    }

    private void replyAccountBanned(LoginSession loginSession) {
        loginSession.channel().writeAndFlush(LoginResultPacket.error(Result.BANNED).build());
    }

    private void checkAccountSuspended(LoginSession loginSession) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime suspensionLiftDateTime = loginSession.playerAccount().suspensionLiftTimestamp();

        if (now.isBefore(suspensionLiftDateTime)) {
            int hours = (int) Duration.between(now, suspensionLiftDateTime).toHours();

            if (hours < 1) {
                hours = 1;
            }
            LoginResultPacket build = LoginResultPacket.error(Result.ACCOUNT_SUSPENDED)
                                                       .suspensionTime(hours)
                                                       .build();
            loginSession.channel().writeAndFlush(build);
        }
    }

    private void unexpectedState(LoginSession loginSession) {
        LOGGER.warn("Unexpected login session state, state={}", loginSession.state());
        loginSession.channel().disconnect();
    }
}
