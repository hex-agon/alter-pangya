package work.fking.pangya.game.player;

import work.fking.pangya.common.Rand;
import work.fking.pangya.game.model.IffContainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static work.fking.pangya.game.model.IffObject.TYPE_CHARACTER;
import static work.fking.pangya.game.model.IffObject.iffTypeFromId;

public class CharacterRoster implements IffContainer<Character> {

    private static final int LUCIA = 67108872;
    private static final int[] LUCIA_BASE_PARTS = {136315904, 136324096, 136332288, 136340480, 136348672, 136356864, 136365056, 136373248, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    private final List<Character> characters = new ArrayList<>();

    @Override
    public List<Character> entries() {
        return Collections.unmodifiableList(characters);
    }

    public void unlockCharacter(int iffId) {
        if (iffTypeFromId(iffId) != TYPE_CHARACTER) {
            throw new IllegalArgumentException("iffId is not a character");
        }
        var character = switch (iffId) {
            case LUCIA -> createBaseLucia();
            default -> throw new IllegalArgumentException("unsupported character iffId=" + iffId);
        };
        characters.add(character);
    }

    private Character createBaseLucia() {
        return new Character(
                Rand.max(30000),
                67108872,
                2,
                new int[] {
                        136315904, 136324096, 136332288, 136340480, 136348672, 136356864, 136365056, 136373248, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                },
                new int[] {
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                },
                new int[] {10, 11, 9, 2, 3},
                0,
                new int[10]
        );
    }
}
