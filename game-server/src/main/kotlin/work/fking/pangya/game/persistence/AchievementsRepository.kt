package work.fking.pangya.game.persistence

import org.jooq.impl.DSL.value
import work.fking.pangya.game.persistence.jooq.tables.references.ACHIEVEMENT
import work.fking.pangya.game.persistence.jooq.tables.references.ACHIEVEMENT_MILESTONE
import work.fking.pangya.game.persistence.jooq.tables.references.PLAYER_ACHIEVEMENT
import work.fking.pangya.game.persistence.jooq.tables.references.PLAYER_ACHIEVEMENT_MILESTONE
import work.fking.pangya.game.player.PlayerAchievement
import work.fking.pangya.game.player.PlayerAchievementMilestone
import work.fking.pangya.game.player.PlayerAchievements
import java.util.stream.Collectors
import java.util.stream.Collectors.groupingBy
import java.util.stream.Collectors.mapping

interface AchievementsRepository {
    fun load(txCtx: TransactionalContext, playerUid: Int): PlayerAchievements
    fun createAchievements(txCtx: TransactionalContext, playerUid: Int)
}

class InMemoryAchievementsRepository : AchievementsRepository {
    override fun load(txCtx: TransactionalContext, playerUid: Int): PlayerAchievements = PlayerAchievements(emptyList())
    override fun createAchievements(txCtx: TransactionalContext, playerUid: Int) {}
}

class JooqAchievementsRepository : AchievementsRepository {
    override fun load(txCtx: TransactionalContext, playerUid: Int): PlayerAchievements {
        val achievements = txCtx.jooq().select(
            PLAYER_ACHIEVEMENT.UID,
            PLAYER_ACHIEVEMENT.IFF_ID,
            PLAYER_ACHIEVEMENT_MILESTONE.UID,
            PLAYER_ACHIEVEMENT_MILESTONE.IFF_ID,
            PLAYER_ACHIEVEMENT_MILESTONE.PROGRESS,
            PLAYER_ACHIEVEMENT_MILESTONE.COMPLETED_AT,
        ).from(PLAYER_ACHIEVEMENT)
            .innerJoin(PLAYER_ACHIEVEMENT_MILESTONE).on(PLAYER_ACHIEVEMENT_MILESTONE.PLAYER_ACHIEVEMENT_UID.eq(PLAYER_ACHIEVEMENT.UID))
            .where(PLAYER_ACHIEVEMENT.ACCOUNT_UID.eq(playerUid))
            .collect(
                groupingBy(
                    { record -> Pair(record[PLAYER_ACHIEVEMENT.UID], record[PLAYER_ACHIEVEMENT.IFF_ID]) },
                    mapping(
                        { record ->
                            PlayerAchievementMilestone(
                                uid = record[PLAYER_ACHIEVEMENT_MILESTONE.UID]!!,
                                iffId = record[PLAYER_ACHIEVEMENT_MILESTONE.IFF_ID]!!,
                                progress = record[PLAYER_ACHIEVEMENT_MILESTONE.PROGRESS]!!,
                                completedAt = record[PLAYER_ACHIEVEMENT_MILESTONE.COMPLETED_AT],
                            )
                        }, Collectors.toList()
                    )
                )
            )

        return PlayerAchievements(achievements.map {
            PlayerAchievement(
                uid = it.key.first!!,
                iffId = it.key.second!!,
                milestones = it.value
            )
        })
    }

    override fun createAchievements(txCtx: TransactionalContext, playerUid: Int) {
        txCtx.jooq().transaction { tx ->
            tx.dsl().insertInto(
                PLAYER_ACHIEVEMENT,
                PLAYER_ACHIEVEMENT.ACCOUNT_UID,
                PLAYER_ACHIEVEMENT.IFF_ID
            ).select(
                tx.dsl().select(value(playerUid), ACHIEVEMENT.IFF_ID).from(ACHIEVEMENT)
            ).execute()

            tx.dsl().insertInto(
                PLAYER_ACHIEVEMENT_MILESTONE,
                PLAYER_ACHIEVEMENT_MILESTONE.PLAYER_ACHIEVEMENT_UID,
                PLAYER_ACHIEVEMENT_MILESTONE.IFF_ID
            ).select(
                tx.dsl().select(
                    PLAYER_ACHIEVEMENT.UID,
                    ACHIEVEMENT_MILESTONE.IFF_ID
                ).from(PLAYER_ACHIEVEMENT)
                    .innerJoin(ACHIEVEMENT_MILESTONE).on(ACHIEVEMENT_MILESTONE.ACHIEVEMENT_IFF_ID.eq(PLAYER_ACHIEVEMENT.IFF_ID))
                    .where(PLAYER_ACHIEVEMENT.ACCOUNT_UID.eq(playerUid))
            ).execute()
        }
    }
}