package work.fking.pangya.login;

import io.netty.channel.Channel;
import work.fking.pangya.common.Rand;
import work.fking.pangya.login.auth.UserInfo;
import work.fking.pangya.login.net.LoginState;

public class Player {

    private final int uid;
    private final Channel channel;

    private final String loginKey;
    private final String sessionKey;
    private final String username;
    private final String nickname;

    private LoginState loginState = LoginState.AUTHENTICATED;

    public Player(Channel channel, UserInfo userInfo) {
        this.channel = channel;
        loginKey = Rand.randomHexString(16);
        sessionKey = Rand.randomHexString(16);
        uid = userInfo.uid();
        username = userInfo.username();
        nickname = userInfo.nickname();
    }

    public int uid() {
        return uid;
    }

    public Channel channel() {
        return channel;
    }

    public String loginKey() {
        return loginKey;
    }

    public String sessionKey() {
        return sessionKey;
    }

    public String username() {
        return username;
    }

    public String nickname() {
        return nickname;
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
}
