package work.fking.pangya.game.persistence

import org.jooq.RecordMapper
import work.fking.pangya.game.persistence.jooq.keys.PLAYER_STATISTICS_PKEY
import work.fking.pangya.game.persistence.jooq.tables.records.PlayerStatisticsRecord
import work.fking.pangya.game.persistence.jooq.tables.references.PLAYER_STATISTICS
import work.fking.pangya.game.player.PlayerStatistics

interface StatisticsRepository {
    fun load(txCtx: TransactionalContext, playerUid: Int): PlayerStatistics
    fun save(txCtx: TransactionalContext, playerUid: Int, statistics: PlayerStatistics)
}

class InMemoryStatisticsRepository : StatisticsRepository {
    override fun load(txCtx: TransactionalContext, playerUid: Int): PlayerStatistics = PlayerStatistics()
    override fun save(txCtx: TransactionalContext, playerUid: Int, statistics: PlayerStatistics) {
    }
}

class JooqStatisticsRepository : StatisticsRepository {
    private val statisticsMapper = RecordMapper<PlayerStatisticsRecord, PlayerStatistics> {
        PlayerStatistics(
            totalShots = it.totalShots,
            totalPutts = it.totalPutts,
            playTimeSeconds = it.playtimeSeconds,
            shotTimeSeconds = it.shotTimeSeconds,
            longestDrive = it.longestDrive,
            pangyaShots = it.pangyaShots,
            timeouts = it.timeouts,
            oobShots = it.oobShots,
            totalDistance = it.totalDistance,
            totalHoles = it.totalHoles,
            unfinishedHoles = it.unfinishedHoles,
            holeInOnes = it.holeInOnes,
            bunkerShots = it.bunkerShots,
            fairwayShots = it.fairwayShots,
            albatross = it.albatross,
            successfulPutts = it.successfulPutts,
            longestPutt = it.longestPutt,
            longestChipIn = it.longestChipIn,
            experience = it.experience,
            level = it.level,
            pangEarned = it.pangEarned,
            totalScore = it.totalScore,
            gamesPlayed = it.gamesPlayed,
            gameComboCurrentStreak = it.gameComboCurrentStreak,
            gameComboBestStreak = it.gameComboBestStreak,
            gamesQuit = it.gamesQuit,
            pangWonInBattle = it.pangWonInBattle,
            pangBattleWins = it.pangBattleWins,
            pangBattleLosses = it.pangBattleLosses,
            pangBattleAllIns = it.pangBattleAllIns,
            pangBattleCombo = it.pangBattleCombo
        )
    }

    override fun load(txCtx: TransactionalContext, playerUid: Int): PlayerStatistics {
        return txCtx.jooq().selectFrom(PLAYER_STATISTICS)
            .where(PLAYER_STATISTICS.ACCOUNT_UID.eq(playerUid))
            .fetchOne(statisticsMapper) ?: throw throw IllegalStateException("could not load statistics for playerId=$playerUid")
    }

    override fun save(txCtx: TransactionalContext, playerUid: Int, statistics: PlayerStatistics) {
        txCtx.jooq().insertInto(PLAYER_STATISTICS)
            .set(PLAYER_STATISTICS.ACCOUNT_UID, playerUid)
            .set(PLAYER_STATISTICS.TOTAL_SHOTS, statistics.totalShots)
            .set(PLAYER_STATISTICS.TOTAL_PUTTS, statistics.totalPutts)
            .set(PLAYER_STATISTICS.PLAYTIME_SECONDS, statistics.playTimeSeconds)
            .set(PLAYER_STATISTICS.SHOT_TIME_SECONDS, statistics.shotTimeSeconds)
            .set(PLAYER_STATISTICS.LONGEST_DRIVE, statistics.longestDrive)
            .set(PLAYER_STATISTICS.PANGYA_SHOTS, statistics.pangyaShots)
            .set(PLAYER_STATISTICS.TIMEOUTS, statistics.timeouts)
            .set(PLAYER_STATISTICS.OOB_SHOTS, statistics.oobShots)
            .set(PLAYER_STATISTICS.TOTAL_DISTANCE, statistics.totalDistance)
            .set(PLAYER_STATISTICS.TOTAL_HOLES, statistics.totalHoles)
            .set(PLAYER_STATISTICS.UNFINISHED_HOLES, statistics.unfinishedHoles)
            .set(PLAYER_STATISTICS.HOLE_IN_ONES, statistics.holeInOnes)
            .set(PLAYER_STATISTICS.BUNKER_SHOTS, statistics.bunkerShots)
            .set(PLAYER_STATISTICS.FAIRWAY_SHOTS, statistics.fairwayShots)
            .set(PLAYER_STATISTICS.ALBATROSS, statistics.albatross)
            .set(PLAYER_STATISTICS.SUCCESSFUL_PUTTS, statistics.successfulPutts)
            .set(PLAYER_STATISTICS.LONGEST_PUTT, statistics.longestPutt)
            .set(PLAYER_STATISTICS.LONGEST_CHIP_IN, statistics.longestChipIn)
            .set(PLAYER_STATISTICS.EXPERIENCE, statistics.experience)
            .set(PLAYER_STATISTICS.LEVEL, statistics.level)
            .set(PLAYER_STATISTICS.PANG_EARNED, statistics.pangEarned)
            .set(PLAYER_STATISTICS.TOTAL_SCORE, statistics.totalScore)
            .set(PLAYER_STATISTICS.GAMES_PLAYED, statistics.gamesPlayed)
            .set(PLAYER_STATISTICS.GAME_COMBO_CURRENT_STREAK, statistics.gameComboCurrentStreak)
            .set(PLAYER_STATISTICS.GAME_COMBO_BEST_STREAK, statistics.gameComboBestStreak)
            .set(PLAYER_STATISTICS.GAMES_QUIT, statistics.gamesQuit)
            .set(PLAYER_STATISTICS.PANG_WON_IN_BATTLE, statistics.pangWonInBattle)
            .set(PLAYER_STATISTICS.PANG_BATTLE_WINS, statistics.pangBattleWins)
            .set(PLAYER_STATISTICS.PANG_BATTLE_LOSSES, statistics.pangBattleLosses)
            .set(PLAYER_STATISTICS.PANG_BATTLE_ALL_INS, statistics.pangBattleAllIns)
            .set(PLAYER_STATISTICS.PANG_BATTLE_COMBO, statistics.pangBattleCombo)
            .onConflict(PLAYER_STATISTICS_PKEY.fields)
            .doUpdate()
            .set(PLAYER_STATISTICS.TOTAL_SHOTS, statistics.totalShots)
            .set(PLAYER_STATISTICS.TOTAL_PUTTS, statistics.totalPutts)
            .set(PLAYER_STATISTICS.PLAYTIME_SECONDS, statistics.playTimeSeconds)
            .set(PLAYER_STATISTICS.SHOT_TIME_SECONDS, statistics.shotTimeSeconds)
            .set(PLAYER_STATISTICS.LONGEST_DRIVE, statistics.longestDrive)
            .set(PLAYER_STATISTICS.PANGYA_SHOTS, statistics.pangyaShots)
            .set(PLAYER_STATISTICS.TIMEOUTS, statistics.timeouts)
            .set(PLAYER_STATISTICS.OOB_SHOTS, statistics.oobShots)
            .set(PLAYER_STATISTICS.TOTAL_DISTANCE, statistics.totalDistance)
            .set(PLAYER_STATISTICS.TOTAL_HOLES, statistics.totalHoles)
            .set(PLAYER_STATISTICS.UNFINISHED_HOLES, statistics.unfinishedHoles)
            .set(PLAYER_STATISTICS.HOLE_IN_ONES, statistics.holeInOnes)
            .set(PLAYER_STATISTICS.BUNKER_SHOTS, statistics.bunkerShots)
            .set(PLAYER_STATISTICS.FAIRWAY_SHOTS, statistics.fairwayShots)
            .set(PLAYER_STATISTICS.ALBATROSS, statistics.albatross)
            .set(PLAYER_STATISTICS.SUCCESSFUL_PUTTS, statistics.successfulPutts)
            .set(PLAYER_STATISTICS.LONGEST_PUTT, statistics.longestPutt)
            .set(PLAYER_STATISTICS.LONGEST_CHIP_IN, statistics.longestChipIn)
            .set(PLAYER_STATISTICS.EXPERIENCE, statistics.experience)
            .set(PLAYER_STATISTICS.LEVEL, statistics.level)
            .set(PLAYER_STATISTICS.PANG_EARNED, statistics.pangEarned)
            .set(PLAYER_STATISTICS.TOTAL_SCORE, statistics.totalScore)
            .set(PLAYER_STATISTICS.GAMES_PLAYED, statistics.gamesPlayed)
            .set(PLAYER_STATISTICS.GAME_COMBO_CURRENT_STREAK, statistics.gameComboCurrentStreak)
            .set(PLAYER_STATISTICS.GAME_COMBO_BEST_STREAK, statistics.gameComboBestStreak)
            .set(PLAYER_STATISTICS.GAMES_QUIT, statistics.gamesQuit)
            .set(PLAYER_STATISTICS.PANG_WON_IN_BATTLE, statistics.pangWonInBattle)
            .set(PLAYER_STATISTICS.PANG_BATTLE_WINS, statistics.pangBattleWins)
            .set(PLAYER_STATISTICS.PANG_BATTLE_LOSSES, statistics.pangBattleLosses)
            .set(PLAYER_STATISTICS.PANG_BATTLE_ALL_INS, statistics.pangBattleAllIns)
            .set(PLAYER_STATISTICS.PANG_BATTLE_COMBO, statistics.pangBattleCombo)
            .execute()
    }
}