package work.fking.pangya.game.persistence

import work.fking.pangya.game.player.PlayerWallet

interface PlayerRepository {
    fun loadWallet(playerId: Int): PlayerWallet
}

class DefaultPlayerRepository : PlayerRepository {
    override fun loadWallet(playerId: Int): PlayerWallet = PlayerWallet()
}