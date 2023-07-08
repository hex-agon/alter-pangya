package work.fking.pangya.game.player

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.model.IffObject
import work.fking.pangya.game.model.Stat

private const val PARTS = 24
private const val STATS = 5
private const val CARDS = 10

class Character(
    override val uid: Int,
    override val iffId: Int,
    private val hairColor: Int,
    val partIffIds: IntArray,
    val partUids: IntArray,
    private val stats: IntArray,
    private val masteryPoints: Int,
    val cardIffIds: IntArray
) : IffObject {

    fun stat(stat: Stat): Int {
        return stats[stat.ordinal]
    }

    fun updateParts(partIffIds: IntArray, partUids: IntArray) {
        partIffIds.copyInto(this.partIffIds)
        partUids.copyInto(this.partUids)
    }

    override fun encode(buffer: ByteBuf) {
        with(buffer) {
            writeIntLE(iffId)
            writeIntLE(uid)
            writeIntLE(hairColor)
            for (iffId in partIffIds) {
                writeIntLE(iffId)
            }
            for (uniqueId in partUids) {
                writeIntLE(uniqueId)
            }
            writeZero(216)
            writeIntLE(0)
            writeIntLE(0)
            writeZero(12)
            writeIntLE(0)
            writeZero(12)
            writeByte(stat(Stat.POWER))
            writeByte(stat(Stat.CONTROL))
            writeByte(stat(Stat.ACCURACY))
            writeByte(stat(Stat.SPIN))
            writeByte(stat(Stat.CURVE))
            writeByte(masteryPoints)
            writeZero(3)
            for (cardIffId in cardIffIds) {
                writeIntLE(cardIffId)
            }
            writeIntLE(0)
            writeIntLE(0)
        }
    }
}


fun ByteBuf.readCharacter(buffer: ByteBuf): Character {
    val iffId = buffer.readIntLE()
    val uid = buffer.readIntLE()
    val hairColor = buffer.readIntLE()
    val partIffIds = IntArray(PARTS)
    for (i in 0 until PARTS) {
        partIffIds[i] = buffer.readIntLE()
    }
    val partUids = IntArray(PARTS)
    for (i in 0 until PARTS) {
        partUids[i] = buffer.readIntLE()
    }
    buffer.skipBytes(216)
    buffer.skipBytes(4)
    buffer.skipBytes(4)
    buffer.skipBytes(12)
    buffer.skipBytes(4)
    buffer.skipBytes(12)
    val stats = IntArray(STATS)
    for (i in 0 until STATS) {
        stats[i] = buffer.readByte().toInt()
    }
    val masteryPoints = buffer.readByte()
    buffer.skipBytes(3)
    val cardIffIds = IntArray(CARDS)
    for (i in 0 until STATS) {
        cardIffIds[i] = buffer.readIntLE()
    }
    buffer.skipBytes(8)
    return Character(
        uid,
        iffId,
        hairColor,
        partIffIds,
        partUids,
        stats,
        masteryPoints.toInt(),
        cardIffIds
    )
}