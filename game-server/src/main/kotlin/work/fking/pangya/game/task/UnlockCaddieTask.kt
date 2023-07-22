package work.fking.pangya.game.task

import work.fking.pangya.game.model.IFF_TYPE_CADDIE
import work.fking.pangya.game.model.iffTypeFromId
import work.fking.pangya.game.persistence.PersistenceContext
import work.fking.pangya.game.player.Caddie
import work.fking.pangya.game.player.Player

class UnlockCaddieTask(
    private val persistenceContext: PersistenceContext,
    private val player: Player,
    private val iffId: Int
) : Runnable {

    override fun run() {
        require(iffTypeFromId(iffId) == IFF_TYPE_CADDIE) { "iffId is not a caddie" }
        val caddie = Caddie(iffId = iffId)
        val persistedCaddie = persistenceContext.caddieRepository.save(player, caddie)
        player.caddieRoster.entries.add(persistedCaddie)
    }
}