package work.fking.pangya.game.player;

import work.fking.pangya.game.model.IffContainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Inventory implements IffContainer<Item> {

    private final List<Item> items = new ArrayList<>();

    @Override
    public List<Item> entries() {
        return Collections.unmodifiableList(items);
    }

    public void add(Item item) {
        items.add(item);
    }

    public boolean existsByUid(int uid) {
        return findByUid(uid) != null;
    }
}
