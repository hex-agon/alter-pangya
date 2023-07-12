package work.fking.pangya.game.player

import work.fking.pangya.game.model.IffContainer
import work.fking.pangya.game.model.achievements
import java.util.concurrent.atomic.AtomicInteger

class PlayerAchievements(override val entries: List<PlayerAchievement>) : IffContainer<PlayerAchievement>

private val achievementUidSequence = AtomicInteger()
private val questUidSequence = AtomicInteger()

fun createPlayerAchievements(): PlayerAchievements = PlayerAchievements(
    achievements.values.map { achievement ->
        PlayerAchievement(
            iffId = achievement.iffId,
            uid = achievementUidSequence.getAndIncrement(),
            quests = achievement.quests.map { quest ->
                PlayerQuest(
                    iffId = quest.iffId,
                    uid = questUidSequence.getAndIncrement()
                )
            }
        )
    }
)
