package work.fking.pangya.login.model;

import io.netty.channel.Channel;

import java.util.function.Consumer;

public record NicknameSetRequest(Channel channel, String nickname, Consumer<Boolean> callback) {

    public static NicknameSetRequest of(Channel channel, String nickname, Consumer<Boolean> callback) {
        return new NicknameSetRequest(channel, nickname, callback);
    }
}
