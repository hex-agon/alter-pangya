package work.fking.pangya.game.model

import io.netty.buffer.ByteBuf

interface IffObject {
    companion object {
        const val TYPE_CHARACTER = 4
        const val TYPE_PART = 8
        const val TYPE_CLUBSET = 16
        const val TYPE_BALL = 20
        const val TYPE_ITEM = 24
        const val TYPE_CADDIE = 28
    }

    val uid: Int

    val iffId: Int

    fun iffTypeId(): Int {
        return iffTypeFromId(iffId)
    }

    fun encode(buffer: ByteBuf)
}

fun iffTypeFromId(iffId: Int): Int {
    return iffId shr 24
}