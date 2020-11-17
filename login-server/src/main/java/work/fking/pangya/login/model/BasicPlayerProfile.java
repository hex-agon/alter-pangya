package work.fking.pangya.login.model;

import work.fking.pangya.common.model.PlayerCharacter;

public record BasicPlayerProfile(PlayerCharacter activeCharacter) {

    public static BasicPlayerProfile of(PlayerCharacter activeCharacter) {
        return new BasicPlayerProfile(activeCharacter);
    }
}
