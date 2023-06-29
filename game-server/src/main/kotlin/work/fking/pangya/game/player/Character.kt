package work.fking.pangya.game.player

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.model.IffObject
import work.fking.pangya.game.model.Stat

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

    override fun encode(buffer: ByteBuf) {
        buffer.writeIntLE(iffId)
        buffer.writeIntLE(uid)
        buffer.writeIntLE(hairColor)
        for (iffId in partIffIds) {
            buffer.writeIntLE(iffId)
        }
        for (uniqueId in partUids) {
            buffer.writeIntLE(uniqueId)
        }
        buffer.writeZero(216)
        buffer.writeIntLE(0)
        buffer.writeIntLE(0)
        buffer.writeZero(12)
        buffer.writeIntLE(0)
        buffer.writeZero(12)
        buffer.writeByte(stat(Stat.POWER))
        buffer.writeByte(stat(Stat.CONTROL))
        buffer.writeByte(stat(Stat.ACCURACY))
        buffer.writeByte(stat(Stat.SPIN))
        buffer.writeByte(stat(Stat.CURVE))
        buffer.writeByte(masteryPoints)
        buffer.writeZero(3)
        for (cardIffId in cardIffIds) {
            buffer.writeIntLE(cardIffId)
        }
        buffer.writeIntLE(0)
        buffer.writeIntLE(0)
    }

    companion object {
        private const val PARTS = 24
        private const val STATS = 5
        private const val CARDS = 10

        @JvmStatic
        fun decode(buffer: ByteBuf): Character {
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
            for (i in stats.indices) {
                stats[i] = buffer.readByte().toInt()
            }
            val masteryPoints = buffer.readByte()
            buffer.skipBytes(3)
            val cardIffIds = IntArray(CARDS)
            for (i in cardIffIds.indices) {
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
    }
}
