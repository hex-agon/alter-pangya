package work.fking.pangya.login.service;

import lombok.extern.log4j.Log4j2;
import work.fking.pangya.login.model.LoginSession;
import work.fking.pangya.login.model.LoginState;
import work.fking.pangya.login.model.NicknameCheckRequest;
import work.fking.pangya.login.model.NicknameSetRequest;
import work.fking.pangya.login.model.PlayerAccount;
import work.fking.pangya.login.packet.outbound.CheckNicknameResultPacket;
import work.fking.pangya.login.packet.outbound.CheckNicknameResultPacket.Result;
import work.fking.pangya.login.repository.PlayerAccountRepository;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.concurrent.ExecutorService;

@Log4j2
@Singleton
public class NicknameService {

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
        CheckNicknameResultPacket result;

        if (exists) {
            result = CheckNicknameResultPacket.error(Result.IN_USE);
        } else {
            result = CheckNicknameResultPacket.available(request.nickname());
        }
        session.updateState(LoginState.SELECTED_NICKNAME);
        request.channel().writeAndFlush(result);
    }

    private void updateNickname(NicknameSetRequest request) {
        LoginSession session = request.channel().attr(LoginSession.KEY).get();

        PlayerAccount playerAccount = session.getPlayerAccount();
        String nickname = request.nickname();

        boolean success = playerRepository.updateNickname(playerAccount.id(), nickname);

        if (success) {
            session.setPlayerAccount(playerAccount.withNickname(nickname));
        }
        request.callback().accept(success);
    }
}
