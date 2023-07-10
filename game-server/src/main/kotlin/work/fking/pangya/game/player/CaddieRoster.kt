package work.fking.pangya.game.player

import work.fking.pangya.common.Rand
import work.fking.pangya.game.model.IFF_TYPE_CADDIE
import work.fking.pangya.game.model.IffContainer
import work.fking.pangya.game.model.iffTypeFromId

class CaddieRoster(override val entries: MutableList<Caddie> = ArrayList()) : IffContainer<Caddie> {

    fun unlockCaddie(iffId: Int) {
        require(iffTypeFromId(iffId) == IFF_TYPE_CADDIE) { "iffId is not a caddie" }
        val caddie = Caddie(uid = Rand.max(10000), iffId = iffId, levelsGained = 4, experience = 1000)
        entries.add(caddie)
    }
}
