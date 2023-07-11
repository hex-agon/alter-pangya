package work.fking.pangya.game.player

import work.fking.pangya.game.model.IFF_TYPE_CADDIE
import work.fking.pangya.game.model.IffContainer
import work.fking.pangya.game.model.iffTypeFromId
import java.util.concurrent.atomic.AtomicInteger

private val uidSequence = AtomicInteger(1)

class CaddieRoster(override val entries: MutableList<Caddie> = ArrayList()) : IffContainer<Caddie> {

    fun unlockCaddie(iffId: Int) {
        require(iffTypeFromId(iffId) == IFF_TYPE_CADDIE) { "iffId is not a caddie" }
        val caddie = Caddie(uid = uidSequence.getAndIncrement(), iffId = iffId, levelsGained = 4, experience = 1000)
        entries.add(caddie)
    }
}
