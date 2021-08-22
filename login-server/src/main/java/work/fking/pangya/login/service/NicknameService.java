package work.fking.pangya.login.service;

import work.fking.pangya.login.model.LoginSession;
import work.fking.pangya.login.model.LoginState;
import work.fking.pangya.login.model.NicknameCheckRequest;
import work.fking.pangya.login.model.NicknameSetRequest;
import work.fking.pangya.login.model.PlayerAccount;
import work.fking.pangya.login.packet.outbound.CheckNicknameReplies;
import work.fking.pangya.login.packet.outbound.CheckNicknameReplies.Error;
import work.fking.pangya.login.repository.PlayerAccountRepository;
import work.fking.pangya.networking.protocol.OutboundPacket;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.concurrent.ExecutorService;
import java.util.regex.Pattern;

@Singleton
public class NicknameService {

    private static final Pattern NICKNAME_PATTERN = Pattern.compile("[\\w()]{4,16}");
    private final PlayerAccountRepository playerRepository;
    private final ExecutorService executorService;

    @Inject
    public NicknameService(PlayerAccountRepository playerRepository, @Named("shared") ExecutorService executorService) {
        this.playerRepository = playerRepository;
        this.executorService = executorService;
    }

    public void queueNicknameCheckRequest(NicknameCheckRequest request) {
        executorService.execute(() -> checkNickname(request));
    }

    public void queueNicknameCheckSet(NicknameSetRequest request) {
        executorService.execute(() -> updateNickname(request));
    }

    private void checkNickname(NicknameCheckRequest request) {
        LoginSession session = request.channel().attr(LoginSession.KEY).get();

        boolean exists = playerRepository.nicknameExists(request.nickname());
        OutboundPacket result;

        if (exists) {
            result = CheckNicknameReplies.error(Error.IN_USE);
        } else if (!NICKNAME_PATTERN.matcher(request.nickname()).matches()) {
            result = CheckNicknameReplies.error(Error.INCORRECT_FORMAT_OR_LENGTH);
        } else {
            result = CheckNicknameReplies.available(request.nickname());
        }
        session.updateState(LoginState.SELECTED_NICKNAME);
        request.channel().writeAndFlush(result);
    }

    private void updateNickname(NicknameSetRequest request) {
        LoginSession session = request.channel().attr(LoginSession.KEY).get();

        PlayerAccount playerAccount = session.playerAccount();
        String nickname = request.nickname();

        boolean success = playerRepository.updateNickname(playerAccount.id(), nickname);

        if (success) {
            session.setPlayerAccount(playerAccount.withNickname(nickname));
        }
        request.callback().accept(success);
    }
}
