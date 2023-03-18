package work.fking.pangya.login.net;

public enum LoginState {
    /**
     * Initial state, authentication must be processed.
     */
    AUTHENTICATING,
    /**
     * Player is now authenticated, he may now proceed to pick a nickname and base character, if needed.
     */
    AUTHENTICATED,
    /**
     * Player is picking a username.
     */
    SELECTING_NICKNAME,
    /**
     * Player picked a valid nickname and can confirm it's choice.
     */
    SELECTED_NICKNAME,
    /**
     * Player is picking a base character.
     */
    SELECTING_CHARACTER,
    /**
     * Player is fully authenticated, has a valid nickname and has a base character.
     */
    LOGGED_IN;

    public boolean validTransition(LoginState to) {
        return switch (this) {
            case AUTHENTICATING -> to == AUTHENTICATED;
            case AUTHENTICATED -> to == SELECTING_NICKNAME || to == SELECTING_CHARACTER || to == LOGGED_IN;
            case SELECTING_NICKNAME -> to == SELECTED_NICKNAME;
            case SELECTED_NICKNAME -> to == LOGGED_IN || to == SELECTING_CHARACTER;
            case SELECTING_CHARACTER -> to == SELECTING_NICKNAME || to == LOGGED_IN;
            case LOGGED_IN -> false;
        };
    }
}
