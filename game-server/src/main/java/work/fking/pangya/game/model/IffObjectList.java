package work.fking.pangya.game.model;

import io.netty.buffer.ByteBuf;

import java.util.List;

public class IffObjectList<T extends IffObject> {

    private final List<T> identifiables;

    public int size() {
        return identifiables.size();
    }

    private IffObjectList(List<T> identifiables) {
        this.identifiables = identifiables;
    }

    public static <T extends IffObject> IffObjectList<T> from(List<T> identifiables) {
        return new IffObjectList<>(identifiables);
    }

    public void encode(ByteBuf buffer) {
        encodePage(buffer, 0, size());
    }

    public void encodePage(ByteBuf buffer, int offset, int limit) {
        var pageSize = limit - offset;
        buffer.writeShortLE(size());
        buffer.writeShortLE(pageSize);

        var subList = identifiables.subList(offset, offset + limit);

        for (T identifiable : subList) {
            identifiable.encode(buffer);
        }
    }
}
