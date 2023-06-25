package work.fking.pangya.game.player;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.common.Rand;
import work.fking.pangya.game.model.IffObject;

public class Item implements IffObject {

    private final int uid;
    private final int iffId;
    private int quantity;

    public Item(int uid, int iffId) {
        this(uid, iffId, 1);
    }

    public Item(int uid, int iffId, int quantity) {
        this.uid = uid;
        this.iffId = iffId;
        this.quantity = quantity;
    }

    public static Item create(int iffId) {
        return new Item(Rand.max(10000), iffId);
    }
    public static Item create(int iffId, int quantity) {
        return new Item(Rand.max(10000), iffId, quantity);
    }

    public int uid() {
        return uid;
    }

    public int iffId() {
        return iffId;
    }

    public int quantity() {
        return quantity;
    }

    public void increment(int delta) {
        this.quantity += delta;
    }

    public void decrement(int delta) {
        this.quantity -= delta;
    }

    @Override
    public void encode(ByteBuf buffer) {
        buffer.writeIntLE(uid);
        buffer.writeIntLE(iffId);
        buffer.writeIntLE(0);
        buffer.writeIntLE(quantity);
        buffer.writeZero(0xb4);
    }
}
