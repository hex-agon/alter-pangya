package work.fking.pangya.login;

import io.netty.channel.Channel;
import work.fking.pangya.common.Rand;
import work.fking.pangya.login.net.LoginState;

public class Player {

    private final int id;
    private final Channel channel;

    private final String loginKey;
    private final String sessionKey;

    private LoginState loginState = LoginState.AUTHENTICATING;

    public Player(int id, Channel channel) {
        this.id = id;
        this.channel = channel;
        loginKey = Rand.randomHexString(16);
        sessionKey = Rand.randomHexString(16);
    }

    public int id() {
        return id;
    }

    public Channel channel() {
        return channel;
    }

    public LoginState loginState() {
        return loginState;
    }

    public void setLoginState(LoginState loginState) {
        if (this.loginState.validTransition(loginState)) {
            this.loginState = loginState;
        } else {
            throw new IllegalStateException("Attempted to set loginState to " + loginState + " from " + this.loginState);
        }
    }

    public String loginKey() {
        return loginKey;
    }

    public String sessionKey() {
        return sessionKey;
    }
}
