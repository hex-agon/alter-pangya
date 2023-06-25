package work.fking.pangya.game.model;

import java.util.List;

public interface IffContainer<I extends IffObject> {

    List<I> entries();

    default I findByIffId(int iffId) {
        for (var entry : entries()) {
            if (entry.iffId() == iffId) {
                return entry;
            }
        }
        return null;
    }

    default I findByUid(int uid) {
        for (var entry : entries()) {
            if (entry.uid() == uid) {
                return entry;
            }
        }
        return null;
    }

    default int size() {
        return entries().size();
    }

    default List<I> chunk(int offset, int size) {
        var entries = entries();
        return entries.subList(offset, Math.min(size, entries.size()));
    }
}
