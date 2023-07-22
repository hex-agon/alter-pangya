package work.fking.pangya.game.persistence

import work.fking.pangya.game.player.PlayerAchievements
import work.fking.pangya.game.player.createPlayerAchievements

interface AchievementsRepository {
    fun load(playerId: Int): PlayerAchievements
}

class DefaultAchievementsRepository : AchievementsRepository {
    override fun load(playerId: Int): PlayerAchievements = createPlayerAchievements()
}