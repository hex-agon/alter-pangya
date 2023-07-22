package work.fking.pangya.game.persistence

import work.fking.pangya.game.player.PlayerStatistics

interface StatisticsRepository {
    fun load(playerId: Int): PlayerStatistics
}

class DefaultStatisticsRepository : StatisticsRepository {
    override fun load(playerId: Int): PlayerStatistics = PlayerStatistics()
}