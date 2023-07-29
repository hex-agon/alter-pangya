package work.fking.pangya.game.player

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.model.IffObject

data class Character(
    override val uid: Int = -1,
    override val iffId: Int,
    val hairColor: Int = 0,
    val partIffIds: IntArray,
    val partUids: IntArray = IntArray(CHARACTER_PARTS_SIZE),
    val auxParts: IntArray = IntArray(CHARACTER_AUX_PARTS_SIZE),
    val cutInIffId: Int = 0,
    val stats: IntArray = IntArray(CHARACTER_STATS_SIZE),
    val mastery: Int = 0,
    val cardIffIds: IntArray = IntArray(CHARACTER_CARDS_SIZE)
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Character

        return uid == other.uid
    }

    override fun hashCode(): Int {
        return uid
    }
}

fun ByteBuf.readCharacter(): Character {
    val iffId = readIntLE()
    val uid = readIntLE()
    val hairColor = readIntLE()
    val partIffIds = IntArray(CHARACTER_PARTS_SIZE) { readIntLE() }
    val partUids = IntArray(CHARACTER_PARTS_SIZE) { readIntLE() }
    skipBytes(216)
    val auxParts = IntArray(CHARACTER_AUX_PARTS_SIZE) { readIntLE() }
    val cutInIffId = readIntLE()
    skipBytes(12)
    val stats = IntArray(CHARACTER_STATS_SIZE) { readUnsignedByte().toInt() }
    val mastery = readIntLE()
    val cardIffIds = IntArray(CHARACTER_CARDS_SIZE) { readIntLE() }
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

fun ByteBuf.write(character: Character) {
    character.encode(this)
}

const val CHARACTER_PARTS_SIZE = 24
const val CHARACTER_AUX_PARTS_SIZE = 5
const val CHARACTER_STATS_SIZE = 5
const val CHARACTER_CARDS_SIZE = 12

const val NURI = 0x4000000
const val HANA = 0x4000001
const val AZER = 0x4000002
const val CECILIA = 0x4000003
const val MAX = 0x4000004
const val KOOH = 0x4000005
const val ARIN = 0x4000006
const val KAZ = 0x4000007
const val LUCIA = 0x4000008
const val NELL = 0x4000009
const val SPIKA = 0x400000a
const val NURI_R = 0x400000b
const val HANA_R = 0x400000c
const val CECILIA_R = 0x400000e

val CHARACTER_BASE_PARTS = mapOf(
    NURI to intArrayOf(134218752, 0, 134235136, 134243328, 134251520, 134259712, 0, 134276096, 134284288),
    HANA to intArrayOf(134480896, 134489088, 134497280, 134505472, 134513664, 0, 134530048, 134538240),
    AZER to intArrayOf(134743040, 134751232, 134759424, 134767616, 134775808, 134784000, 134792192, 134800384),
    CECILIA to intArrayOf(135005184, 135013376, 135021568, 135029760, 135037952, 135046144, 135054336, 135062528),
    MAX to intArrayOf(135267328, 135275520, 135283712, 135291904, 135300096, 135308288),
    KOOH to intArrayOf(135529472, 135537664, 135545856, 135554048, 135562240, 135570432, 135578624, 135586816),
    ARIN to intArrayOf(135791616, 135799808, 135808000, 135816192, 135824384, 135832576, 135840768),
    KAZ to intArrayOf(136053760, 136061952, 136070144, 136078336, 136086528, 136094720, 136102912, 136111104, 136119296),
    LUCIA to intArrayOf(136315904, 136324096, 136332288, 136340480, 136348672, 136356864, 136365056, 136373248),
    NELL to intArrayOf(136578048, 136586240, 136594432, 136602624, 136610816, 136619008, 136627200, 136635392),
    SPIKA to intArrayOf(136840192, 136848384, 136856576, 136864768, 136872960, 136881152, 136889344, 136897536),
    NURI_R to intArrayOf(137102336, 137110528, 137118720, 137126912, 137135104, 137143296, 137151488, 137159680),
    HANA_R to intArrayOf(137364480, 137372672, 137380864, 137389056, 137397248, 137405440, 137413632, 137421824),
    CECILIA_R to intArrayOf(137888768, 137896960, 137905152, 137913344, 137921536, 137929728, 137937920, 137946112)
)

fun characterBaseParts(iffId: Int): IntArray {
    val baseIffIds = CHARACTER_BASE_PARTS[iffId] ?: throw IllegalStateException("Missing base parts for character $iffId")
    val partIffIds = IntArray(CHARACTER_PARTS_SIZE)
    baseIffIds.copyInto(partIffIds)
    return partIffIds
}