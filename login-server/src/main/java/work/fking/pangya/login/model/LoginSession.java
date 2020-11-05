package work.fking.pangya.login.model;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

public class LoginSession {

    public static final AttributeKey<LoginSession> KEY = AttributeKey.valueOf("loginSession");

    private final Channel channel;

    private PlayerAccount playerAccount;
    private LoginState state;

    private LoginSession(Channel channel) {
        this.channel = channel;
        this.state = LoginState.AUTHENTICATING;
    }

    public static LoginSession create(Channel channel) {
        return new LoginSession(channel);
    }

    public Channel getChannel() {
        return channel;
    }

    public PlayerAccount getPlayerAccount() {
        return playerAccount;
    }

    public void setPlayerAccount(PlayerAccount playerAccount) {
        this.playerAccount = playerAccount;
    }

    public LoginState getState() {
        return state;
    }

    public void updateState(LoginState loginState) {
        this.state = loginState;
    }
}
