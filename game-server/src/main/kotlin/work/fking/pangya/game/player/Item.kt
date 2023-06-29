package work.fking.pangya.game.player

import io.netty.buffer.ByteBuf
import work.fking.pangya.common.Rand
import work.fking.pangya.game.model.IffObject

class Item(
    override val uid: Int = Rand.max(10000),
    override val iffId: Int,
    private var quantity: Int = 1
) : IffObject {

    fun quantity(): Int {
        return quantity
    }

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
        buffer.writeIntLE(quantity)
        buffer.writeZero(0xb4)
    }
}
