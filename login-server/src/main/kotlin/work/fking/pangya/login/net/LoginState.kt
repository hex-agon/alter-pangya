package work.fking.pangya.login.net

enum class LoginState {
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
    LOGGED_IN,

    /**
     * Player has been handed over to a game server.
     */
    HANDED_OVER;

    fun validTransition(to: LoginState): Boolean {
        return when (this) {
            to -> true
            AUTHENTICATED -> to == SELECTING_NICKNAME || to == SELECTING_CHARACTER || to == LOGGED_IN
            SELECTING_NICKNAME -> to == SELECTED_NICKNAME
            SELECTED_NICKNAME -> to == LOGGED_IN || to == SELECTING_CHARACTER
            SELECTING_CHARACTER -> to == SELECTING_NICKNAME || to == LOGGED_IN
            LOGGED_IN -> to == HANDED_OVER
            HANDED_OVER -> false
        }
    }
}
