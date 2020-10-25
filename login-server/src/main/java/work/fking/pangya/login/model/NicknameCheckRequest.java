package work.fking.pangya.login.model;

import io.netty.channel.Channel;

public record NicknameCheckRequest(Channel channel, String nickname) {

    public static NicknameCheckRequest of(Channel channel, String nickname) {
        return new NicknameCheckRequest(channel, nickname);
    }
}
