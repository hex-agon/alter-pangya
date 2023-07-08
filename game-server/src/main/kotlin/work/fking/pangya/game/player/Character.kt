package work.fking.pangya.game.player

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.model.IffObject

private const val PARTS = 24
private const val AUX_PARTS = 5
private const val STATS = 5
private const val CARDS = 12

class Character(
    override val uid: Int,
    override val iffId: Int,
    private val hairColor: Int = 0,
    val partIffIds: IntArray,
    val partUids: IntArray = IntArray(PARTS),
    val auxParts: IntArray = IntArray(AUX_PARTS),
    val previewIffId: Int = 0,
    private val stats: IntArray = IntArray(STATS),
    val cardIffIds: IntArray = IntArray(CARDS)
) : IffObject {

    fun updateParts(partIffIds: IntArray, partUids: IntArray) {
        partIffIds.copyInto(this.partIffIds)
        partUids.copyInto(this.partUids)
    }

    override fun encode(buffer: ByteBuf) {
        with(buffer) {
            writeIntLE(iffId)
            writeIntLE(uid)
            writeIntLE(hairColor)
            partIffIds.forEach { writeIntLE(it) }
            partUids.forEach { writeIntLE(it) }
            writeZero(216)
            auxParts.forEach { writeIntLE(it) }
            writeIntLE(previewIffId)
            writeZero(16)
            stats.forEach { writeByte(it) }
            cardIffIds.forEach { writeIntLE(it) }
        }
    }
}

fun ByteBuf.readCharacter(): Character {
    val iffId = readIntLE()
    val uid = readIntLE()
    val hairColor = readIntLE()
    val partIffIds = IntArray(PARTS) { readIntLE() }
    val partUids = IntArray(PARTS) { readIntLE() }
    skipBytes(216)
    val auxParts = IntArray(AUX_PARTS) { readIntLE() }
    val previewIffId = readIntLE()
    skipBytes(16)
    val stats = IntArray(STATS) { readUnsignedByte().toInt() }
    val cardIffIds = IntArray(CARDS) { readIntLE() }
    return Character(
        uid = uid,
        iffId = iffId,
        hairColor = hairColor,
        partIffIds = partIffIds,
        partUids = partUids,
        auxParts = auxParts,
        previewIffId = previewIffId,
        stats = stats,
        cardIffIds = cardIffIds
    )
}