package work.fking.pangya.login.service;

import io.netty.channel.Channel;
import work.fking.pangya.login.model.BasicPlayerProfile;
import work.fking.pangya.login.model.LoginSession;
import work.fking.pangya.login.model.NewProfileRequest;
import work.fking.pangya.login.packet.outbound.ConfirmCharacterSelectionPacket;
import work.fking.pangya.login.repository.PlayerProfileRepository;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.concurrent.ExecutorService;

@Singleton
public class ProfileService {

    private final PlayerProfileRepository profileRepository;
    private final ExecutorService executorService;

    @Inject
    public ProfileService(PlayerProfileRepository profileRepository, @Named("shared") ExecutorService executorService) {
        this.profileRepository = profileRepository;
        this.executorService = executorService;
    }

    public void queueNewProfileRequest(NewProfileRequest request) {
        executorService.execute(() -> createProfile(request));
    }

    private void createProfile(NewProfileRequest request) {
        Channel channel = request.channel();
        LoginSession session = channel.attr(LoginSession.KEY).get();

        boolean result = profileRepository.createProfile(session.getPlayerAccount().id(), BasicPlayerProfile.of(request.character()));
        channel.write(ConfirmCharacterSelectionPacket.instance());
        request.callback().accept(result);
    }
}
