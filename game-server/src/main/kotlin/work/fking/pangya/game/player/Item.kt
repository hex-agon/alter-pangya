package work.fking.pangya.game.player

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.model.IFF_TYPE_BALL
import work.fking.pangya.game.model.IFF_TYPE_ITEM
import work.fking.pangya.game.model.IFF_TYPE_PASSIVE_ITEM
import work.fking.pangya.game.model.IffObject
import work.fking.pangya.game.model.iffTypeFromId
import java.util.concurrent.atomic.AtomicInteger

private val uidSequence = AtomicInteger(1)

class Item(
    override val uid: Int = uidSequence.getAndIncrement(),
    override val iffId: Int,
    private var quantity: Int = 0
) : IffObject {

    fun increment(delta: Int) {
        quantity += delta
    }

    fun decrement(delta: Int) {
        quantity -= delta
    }

    override fun encode(buffer: ByteBuf) {
        buffer.writeIntLE(uid)
        buffer.writeIntLE(iffId)
        buffer.writeIntLE(0)
        // if the item is not quantifiable, this is the item current stats
        if (iffTypeFromId(iffId) in intArrayOf(IFF_TYPE_ITEM, IFF_TYPE_PASSIVE_ITEM, IFF_TYPE_BALL)) {
            buffer.writeIntLE(quantity)
            buffer.writeZero(180)
        } else {
            // item stats
            buffer.writeShortLE(1)
            buffer.writeShortLE(1)
            buffer.writeShortLE(1)
            buffer.writeShortLE(1)
            buffer.writeShortLE(1)
            buffer.writeZero(174)
        }
    }
}
