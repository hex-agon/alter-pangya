package work.fking.pangya.login.model;

import java.time.LocalDateTime;
import java.util.Arrays;

public record PlayerAccount(long id, String username, String nickname, byte[] passwordHash, Status status, LocalDateTime suspensionLiftTimestamp) {

    public enum Status {
        ACTIVE,
        SUSPENDED,
        DISABLED
    }

    public void clearPasswordHash() {
        Arrays.fill(passwordHash, (byte) '0');
    }
}
