package work.fking.pangya.game.player

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.model.IFF_TYPE_BALL
import work.fking.pangya.game.model.IFF_TYPE_EQUIPITEM_ITEM
import work.fking.pangya.game.model.IFF_TYPE_NOEQUIP_ITEM
import work.fking.pangya.game.model.IffObject
import work.fking.pangya.game.model.iffTypeFromId

class Item(
    override val uid: Int = -1,
    override val iffId: Int,
    var quantity: Int = 0,
    val stats: IntArray = IntArray(5),
    val cards: IntArray = IntArray(12),
    var ucc: ItemUcc = nullItemUcc(),
    var clubWorkshop: ItemClubWorkshop = nullItemClubWorkshop()
) : IffObject {

    fun quantifiable(): Boolean {
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
            writeZero(19)

            write(ucc)
            cards.forEach { writeIntLE(it) }
            write(clubWorkshop)
        }
    }

    fun copy(uid: Int): Item {
        return Item(
            uid = uid,
            iffId = iffId,
            quantity = quantity,
            stats = stats
        )
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

