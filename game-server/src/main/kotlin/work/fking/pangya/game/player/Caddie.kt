package work.fking.pangya.game.player

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.model.IffObject

data class Caddie(
    override val uid: Int = -1,
    override val iffId: Int,
    val level: Int = 0,
    val experience: Int = 0
) : IffObject {

    override fun encode(buffer: ByteBuf) {
        buffer.writeIntLE(uid)
        buffer.writeIntLE(iffId)
        buffer.writeIntLE(0)
        buffer.writeByte(level)
        buffer.writeIntLE(experience)
        buffer.writeLongLE(0)
    }
}

fun nullCaddie() = Caddie(iffId = 0)
