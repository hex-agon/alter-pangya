package work.fking.pangya.login.model;

import io.netty.channel.Channel;

import java.util.Arrays;

public record LoginRequest(Channel channel, String username, char[] passwordMd5) {

    public static LoginRequest of(Channel channel, String username, char[] passwordMd5) {
        return new LoginRequest(channel, username, passwordMd5);
    }

    public void clearPasswordMd5() {
        Arrays.fill(passwordMd5, '0');
    }
}
