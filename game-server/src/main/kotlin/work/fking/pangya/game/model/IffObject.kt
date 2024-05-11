package work.fking.pangya.game.model

import io.netty.buffer.ByteBuf

enum class IffType {
    CHARACTER,
    PART,
    CLUBSET,
    BALL,
    EQUIPITEM_ITEM,
    NOEQUIP_ITEM,
    CADDIE,
    CARD
}

interface IffObject {

    val uid: Int

    val iffId: Int

    fun iffTypeId() = iffTypeFromId(iffId)

    fun encode(buffer: ByteBuf)
}

fun iffTypeFromId(iffId: Int): IffType {
    return when (val typeId = iffId shr 24) {
        4 -> IffType.CHARACTER
        8 -> IffType.PART
        16 -> IffType.CLUBSET
        20 -> IffType.BALL
        24 -> IffType.EQUIPITEM_ITEM
        26 -> IffType.NOEQUIP_ITEM
        28 -> IffType.CADDIE
        124 -> IffType.CARD
        else -> throw IllegalArgumentException("Unknown iffTypeId $typeId")
    }
}