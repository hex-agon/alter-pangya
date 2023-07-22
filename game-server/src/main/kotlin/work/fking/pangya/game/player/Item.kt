package work.fking.pangya.game.player

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.model.IFF_TYPE_BALL
import work.fking.pangya.game.model.IFF_TYPE_EQUIPITEM_ITEM
import work.fking.pangya.game.model.IFF_TYPE_NOEQUIP_ITEM
import work.fking.pangya.game.model.IffObject
import work.fking.pangya.game.model.iffTypeFromId

data class Item(
    override val uid: Int = -1,
    override val iffId: Int,
    val quantity: Int = 0,
    val stats: IntArray = IntArray(5)
) : IffObject {

    private fun quantifiable(): Boolean {
        return when (iffTypeFromId(iffId)) {
            IFF_TYPE_EQUIPITEM_ITEM, IFF_TYPE_NOEQUIP_ITEM, IFF_TYPE_BALL -> true
            else -> false
        }
    }

    override fun encode(buffer: ByteBuf) {
        with(buffer) {
            writeIntLE(uid)
            writeIntLE(iffId)
            writeIntLE(0)

            if (quantifiable()) {
                writeIntLE(quantity)
                writeZero(6)
            } else {
                stats.forEach { buffer.writeShortLE(it) }
            }
            writeZero(174)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Item

        return uid == other.uid
    }

    override fun hashCode(): Int {
        return uid
    }
}
