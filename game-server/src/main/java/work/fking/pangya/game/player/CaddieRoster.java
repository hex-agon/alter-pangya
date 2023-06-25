package work.fking.pangya.game.player;

import work.fking.pangya.common.Rand;
import work.fking.pangya.game.model.IffContainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static work.fking.pangya.game.model.IffObject.TYPE_CADDIE;
import static work.fking.pangya.game.model.IffObject.iffTypeFromId;

public class CaddieRoster implements IffContainer<Caddie> {

    private final List<Caddie> caddies = new ArrayList<>();

    @Override
    public List<Caddie> entries() {
        return Collections.unmodifiableList(caddies);
    }

    public void unlockCaddie(int iffId) {
        if (iffTypeFromId(iffId) != TYPE_CADDIE) {
            throw new IllegalArgumentException("iffId is not a caddie");
        }
        var caddie = new Caddie(Rand.max(10000), iffId, 0, 0);
        caddies.add(caddie);
    }
}
