package work.fking.common.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class AccountProfile {

    private String username;

    private String nickname;

    private Status status;

    private int activeCharacterId;

    private LocalDateTime suspensionLiftDateTime;

    private List<String> chatMacros;

    public enum Status {
        ACTIVE,
        SUSPENDED,
        DISABLED
    }
}
