package work.fking.pangya.login.model;

import io.netty.channel.Channel;

import java.util.function.Consumer;

public record NewProfileRequest(Channel channel, int characterIffId, int characterHairColor, Consumer<Boolean> callback) {

}
