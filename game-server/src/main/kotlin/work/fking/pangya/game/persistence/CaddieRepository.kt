package work.fking.pangya.game.persistence

import work.fking.pangya.game.player.Caddie
import work.fking.pangya.game.player.CaddieRoster
import work.fking.pangya.game.player.Player
import java.util.concurrent.atomic.AtomicInteger

interface CaddieRepository {
    fun loadRoster(playerId: Int): CaddieRoster
    fun save(player: Player, caddie: Caddie): Caddie
}

class DefaultCaddieRepository : CaddieRepository {
    private val uidSequence = AtomicInteger(1)

    override fun loadRoster(playerId: Int): CaddieRoster = CaddieRoster()
    override fun save(player: Player, caddie: Caddie): Caddie = caddie.copy(uid = uidSequence.getAndIncrement())
}