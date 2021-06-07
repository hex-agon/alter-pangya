package work.fking.pangya.login.model;

import io.netty.channel.Channel;
import work.fking.pangya.common.model.player.PlayerCharacter;

import java.util.function.Consumer;

public record NewProfileRequest(Channel channel, PlayerCharacter character, Consumer<Boolean> callback) {

}
