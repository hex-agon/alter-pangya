package work.fking.pangya.game.player

import io.netty.buffer.ByteBuf
import work.fking.pangya.common.Rand
import work.fking.pangya.game.model.IFF_TYPE_CLUBSET
import work.fking.pangya.game.model.IFF_TYPE_ITEM
import work.fking.pangya.game.model.IffObject
import work.fking.pangya.game.model.iffTypeFromId

class Item(
    override val uid: Int = Rand.max(10000),
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
        if (iffTypeFromId(iffId) == IFF_TYPE_ITEM) {
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
