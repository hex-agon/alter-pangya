package work.fking.pangya.game.player

import work.fking.pangya.common.Rand
import work.fking.pangya.game.model.IFF_TYPE_CHARACTER
import work.fking.pangya.game.model.IffContainer
import work.fking.pangya.game.model.iffTypeFromId

private const val NURI = 0x4000000
private const val HANA = 0x4000001
private const val AZER = 0x4000002
private const val CECILIA = 0x4000003
private const val MAX = 0x4000004
private const val KOOH = 0x4000005
private const val ARIN = 0x4000006
private const val KAZ = 0x4000007
private const val LUCIA = 0x4000008
private const val NELL = 0x4000009
private const val SPIKA = 0x400000a
private const val NURI_R = 0x400000b
private const val HANA_R = 0x400000c
private const val CECILIA_R = 0x400000e

private val baseParts = mapOf(
    NURI to intArrayOf(134218752, 0, 134235136, 134243328, 134251520, 134259712, 0, 134276096, 134284288, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
    HANA to intArrayOf(134480896, 134489088, 134497280, 134505472, 134513664, 0, 134530048, 134538240, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
    AZER to intArrayOf(134743040, 134751232, 134759424, 134767616, 134775808, 134784000, 134792192, 134800384, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
    CECILIA to intArrayOf(135005184, 135013376, 135021568, 135029760, 135037952, 135046144, 135054336, 135062528, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
    MAX to intArrayOf(135267328, 135275520, 135283712, 135291904, 135300096, 135308288, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
    KOOH to intArrayOf(135529472, 135537664, 135545856, 135554048, 135562240, 135570432, 135578624, 135586816, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
    ARIN to intArrayOf(135791616, 135799808, 135808000, 135816192, 135824384, 135832576, 135840768, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
    KAZ to intArrayOf(136053760, 136061952, 136070144, 136078336, 136086528, 136094720, 136102912, 136111104, 136119296, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
    LUCIA to intArrayOf(136315904, 136324096, 136332288, 136340480, 136348672, 136356864, 136365056, 136373248, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
    NELL to intArrayOf(136578048, 136586240, 136594432, 136602624, 136610816, 136619008, 136627200, 136635392, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
    SPIKA to intArrayOf(136840192, 136848384, 136856576, 136864768, 136872960, 136881152, 136889344, 136897536, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
    NURI_R to intArrayOf(137102336, 137110528, 137118720, 137126912, 137135104, 137143296, 137151488, 137159680, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
    HANA_R to intArrayOf(137364480, 137372672, 137380864, 137389056, 137397248, 137405440, 137413632, 137421824, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
    CECILIA_R to intArrayOf(137888768, 137896960, 137905152, 137913344, 137921536, 137929728, 137937920, 137946112, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
)

class CharacterRoster(override val entries: MutableList<Character> = ArrayList()) : IffContainer<Character> {

    fun unlockCharacter(iffId: Int) {
        require(iffTypeFromId(iffId) == IFF_TYPE_CHARACTER) { "iffId is not a character" }
        val character = Character(
            uid = Rand.max(30000),
            iffId = iffId,
            partIffIds = baseParts[iffId]!!,
        )
        entries.add(character)
    }
}