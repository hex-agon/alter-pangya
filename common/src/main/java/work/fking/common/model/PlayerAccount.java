package work.fking.common.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PlayerAccount {

    private String username;

    private String nickname;

    private Status status;

    private LocalDateTime suspensionLiftDateTime;

    public enum Status {
        ACTIVE,
        SUSPENDED,
        DISABLED
    }
}
