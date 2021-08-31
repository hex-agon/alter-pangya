package work.fking.pangya.login.model;

import io.netty.util.AttributeKey;

public enum ConnectionState {
    AUTHENTICATING,
    AUTHENTICATED,
    SELECTING_NICKNAME,
    SELECTED_NICKNAME,
    SELECTING_CHARACTER,
    LOGGED_IN;

    public static final AttributeKey<ConnectionState> KEY = AttributeKey.valueOf("connectionState");
}
