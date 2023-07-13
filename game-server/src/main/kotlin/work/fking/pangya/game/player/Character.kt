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
    private val partIffIds: IntArray,
    private val partUids: IntArray = IntArray(PARTS),
    private val auxParts: IntArray = IntArray(AUX_PARTS),
    private val cutInIffId: Int = 0,
    private val stats: IntArray = IntArray(STATS) { 30 },
    private val mastery: Int = 10,
    private val cardIffIds: IntArray = IntArray(CARDS)
) : IffObject {

    fun updateParts(character: Character) {
        character.partIffIds.copyInto(partIffIds)
        character.partUids.copyInto(partUids)
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
            writeIntLE(cutInIffId)
            writeZero(12)
            stats.forEach { writeByte(it) }
            writeIntLE(mastery)
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
    val cutInIffId = readIntLE()
    skipBytes(12)
    val stats = IntArray(STATS) { readUnsignedByte().toInt() }
    val mastery = readIntLE()
    val cardIffIds = IntArray(CARDS) { readIntLE() }
    return Character(
        uid = uid,
        iffId = iffId,
        hairColor = hairColor,
        partIffIds = partIffIds,
        partUids = partUids,
        auxParts = auxParts,
        cutInIffId = cutInIffId,
        stats = stats,
        mastery = mastery,
        cardIffIds = cardIffIds
    )
}