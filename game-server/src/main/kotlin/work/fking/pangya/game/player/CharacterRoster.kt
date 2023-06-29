package work.fking.pangya.game.player

import work.fking.pangya.common.Rand
import work.fking.pangya.game.model.IffContainer
import work.fking.pangya.game.model.IffObject
import work.fking.pangya.game.model.iffTypeFromId

class CharacterRoster(override val entries: MutableList<Character> = ArrayList()) : IffContainer<Character> {

    companion object {
        private const val LUCIA = 67108872
        private val LUCIA_BASE_PARTS = intArrayOf(136315904, 136324096, 136332288, 136340480, 136348672, 136356864, 136365056, 136373248, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    }

    fun unlockCharacter(iffId: Int) {
        require(iffTypeFromId(iffId) == IffObject.TYPE_CHARACTER) { "iffId is not a character" }
        val character = when (iffId) {
            LUCIA -> createBaseLucia()
            else -> throw IllegalArgumentException("unsupported character iffId=$iffId")
        }
        entries.add(character)
    }

    private fun createBaseLucia(): Character {
        return Character(
            uid = Rand.max(30000),
            iffId = 67108872,
            hairColor = 2,
            partIffIds = intArrayOf(136315904, 136324096, 136332288, 136340480, 136348672, 136356864, 136365056, 136373248, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            partUids = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            stats = intArrayOf(10, 11, 9, 2, 3),
            masteryPoints = 0, cardIffIds = IntArray(10)
        )
    }
}
