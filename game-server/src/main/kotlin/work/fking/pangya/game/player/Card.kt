package work.fking.pangya.game.player

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.model.IffObject

data class Card(
        override val uid: Int,
        override val iffId: Int,
        val quantity: Int
) : IffObject {

    override fun encode(buffer: ByteBuf) {
        with(buffer) {
            writeIntLE(uid)
            writeIntLE(iffId)
            writeZero(12)
            writeIntLE(quantity)
            writeZero(32)
            writeShortLE(1)
        }
    }
}
