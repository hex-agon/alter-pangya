package work.fking.pangya.login.model;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import lombok.Getter;

@Getter
public class LoginSession {

    public static final AttributeKey<LoginSession> KEY = AttributeKey.valueOf("loginSession");

    private final Channel channel;

    private LoginState state;

    private LoginSession(Channel channel) {
        this.channel = channel;
        this.state = LoginState.AUTHENTICATING;
    }

    public static LoginSession create(Channel channel) {
        return new LoginSession(channel);
    }

    public void updateState(LoginState loginState) {
        this.state = loginState;
    }
}
