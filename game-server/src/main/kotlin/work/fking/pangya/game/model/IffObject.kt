package work.fking.pangya.game.model

import io.netty.buffer.ByteBuf

const val IFF_TYPE_CHARACTER = 4
const val IFF_TYPE_PART = 8
const val IFF_TYPE_CLUBSET = 16
const val IFF_TYPE_BALL = 20
const val IFF_TYPE_ITEM = 24
const val IFF_TYPE_PASSIVE_ITEM = 26
const val IFF_TYPE_CADDIE = 28

interface IffObject {

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