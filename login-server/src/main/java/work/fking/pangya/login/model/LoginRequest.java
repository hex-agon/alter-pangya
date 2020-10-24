package work.fking.pangya.login.model;

import java.nio.channels.Channel;

public record LoginRequest(Channel channel, String username, char[] passwordMd5) {

    public static LoginRequest of(Channel channel, String username, char[] passwordMd5) {
        return new LoginRequest(channel, username, passwordMd5);
    }
}
