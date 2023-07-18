package work.fking.pangya.game.player

import io.netty.buffer.ByteBuf
import kotlin.math.max

data class PlayerStatistics(
    val totalShots: Int = 0,
    val totalPutts: Int = 0,
    val playTimeSeconds: Int = 0,
    val shotTimeSeconds: Int = 0,
    val longestDrive: Float = 0f,
    val pangyaShots: Int = 0,
    val timeouts: Int = 0,
    val oobShots: Int = 0,
    val totalDistance: Int = 0,
    val totalHoles: Int = 0,
    val unfinishedHoles: Int = 0,
    val holeInOnes: Int = 0,
    val bunkerShots: Int = 0,
    val fairwayShots: Int = 0,
    val albatross: Int = 0,
    val successfulPutts: Int = 0,
    val longestPutt: Float = 0f,
    val longestChipIn: Float = 0f,
    val experience: Int = 0,
    val level: Int = 0,
    val pangEarned: Long = 0,
    val totalScore: Int = 0,
    val gamesPlayed: Int = 0,
    val gameComboCurrentStreak: Int = 0,
    val gameComboBestStreak: Int = 0,
    val gamesQuit: Int = 0,
    val pangWonInBattle: Int = 0,
    val pangBattleWins: Int = 0,
    val pangBattleLosses: Int = 0,
    val pangBattleAllIns: Int = 0,
    val pangBattleCombo: Int = 0,
)

operator fun PlayerStatistics.plus(other: PlayerStatistics): PlayerStatistics {
    return PlayerStatistics(
        totalShots = totalShots + other.totalShots,
        totalPutts = totalPutts + other.totalPutts,
        playTimeSeconds = playTimeSeconds + other.playTimeSeconds,
        shotTimeSeconds = shotTimeSeconds + other.shotTimeSeconds,
        longestDrive = max(longestDrive, other.longestDrive),
        pangyaShots = pangyaShots + other.pangyaShots,
        timeouts = timeouts + other.timeouts,
        oobShots = oobShots + other.oobShots,
        totalDistance = totalDistance + other.totalDistance,
        totalHoles = totalHoles + other.totalHoles,
        unfinishedHoles = unfinishedHoles + other.unfinishedHoles,
        holeInOnes = holeInOnes + other.holeInOnes,
        bunkerShots = bunkerShots + other.bunkerShots,
        fairwayShots = fairwayShots + other.fairwayShots,
        albatross = albatross + other.albatross,
        successfulPutts = successfulPutts + other.successfulPutts,
        longestPutt = max(longestPutt, other.longestPutt),
        longestChipIn = max(longestChipIn, other.longestChipIn),
        experience = experience,
        level = level,
        pangEarned = pangEarned + other.pangEarned,
        totalScore = totalScore + other.totalScore,
        gamesPlayed = gamesPlayed + other.gamesPlayed,
        gameComboCurrentStreak = max(gameComboCurrentStreak, other.gameComboCurrentStreak),
        gameComboBestStreak = max(gameComboBestStreak, other.gameComboBestStreak),
        gamesQuit = gamesQuit + other.gamesQuit,
        pangWonInBattle = pangWonInBattle + other.pangWonInBattle,
        pangBattleWins = pangBattleWins + other.pangBattleWins,
        pangBattleLosses = pangBattleLosses + other.pangBattleLosses,
        pangBattleAllIns = pangBattleAllIns + other.pangBattleAllIns,
        pangBattleCombo = pangBattleCombo + other.pangBattleCombo,
    )
}

fun ByteBuf.readStatistics(): PlayerStatistics {
    val totalShots = readIntLE()
    val totalPutts = readIntLE()
    val playTimeSeconds = readIntLE()
    val shotTimeSeconds = readIntLE()
    val longestDrive = readFloatLE()
    val pangyaShots = readIntLE()
    val timeouts = readIntLE()
    val oobShots = readIntLE()
    val totalDistance = readIntLE()
    val totalHoles = readIntLE()
    val unfinishedHoles = readIntLE()
    val holeInOnes = readIntLE()
    val bunkerShots = readShortLE().toInt()
    val fairwayShots = readIntLE()
    val albatross = readIntLE()
    readIntLE()
    val successfulPutts = readIntLE()
    val longestPutt = readFloatLE()
    val longestChipIn = readFloatLE()
    val experience = readIntLE()
    val level = readByte().toInt()
    val pangEarned = readLongLE()
    val totalScore = readIntLE()
    val bestScorePerDifficulty = IntArray(5) { readByte().toInt() }
    readByte()
    val bestPangPerDifficulty = LongArray(5) { readLongLE() }
    val bestPangTotal = readLongLE()
    val gamesPlayed = readIntLE()
    readIntLE()
    readIntLE()
    readIntLE()
    readIntLE()
    readIntLE()
    readIntLE()
    readIntLE()
    readIntLE()
    val gameComboCurrentStreak = readIntLE()
    val gameComboBestStreak = readIntLE()
    val gamesQuit = readIntLE()
    val pangWonInBattle = readIntLE()
    val pangBattleWins = readIntLE()
    val pangBattleLosses = readIntLE()
    val pangBattleAllIns = readIntLE()
    val pangBattleCombo = readIntLE()
    readIntLE()
    skipBytes(10)
    readIntLE()
    skipBytes(8)
    return PlayerStatistics(
        totalShots = totalShots,
        totalPutts = totalPutts,
        playTimeSeconds = playTimeSeconds,
        shotTimeSeconds = shotTimeSeconds,
        longestDrive = longestDrive,
        pangyaShots = pangyaShots,
        timeouts = timeouts,
        oobShots = oobShots,
        totalDistance = totalDistance,
        totalHoles = totalHoles,
        unfinishedHoles = unfinishedHoles,
        holeInOnes = holeInOnes,
        bunkerShots = bunkerShots,
        fairwayShots = fairwayShots,
        albatross = albatross,
        successfulPutts = successfulPutts,
        longestPutt = longestPutt,
        longestChipIn = longestChipIn,
        experience = experience,
        level = level,
        pangEarned = pangEarned,
        totalScore = totalScore,
        gamesPlayed = gamesPlayed,
        gameComboCurrentStreak = gameComboCurrentStreak,
        gameComboBestStreak = gameComboBestStreak,
        gamesQuit = gamesQuit,
        pangWonInBattle = pangWonInBattle,
        pangBattleWins = pangBattleWins,
        pangBattleLosses = pangBattleLosses,
        pangBattleAllIns = pangBattleAllIns,
        pangBattleCombo = pangBattleCombo,
    )
}

fun ByteBuf.write(statistics: PlayerStatistics) {
    with(statistics) {
        writeIntLE(totalShots) // total shots !
        writeIntLE(totalPutts) // total putts !
        writeIntLE(playTimeSeconds) // total play time, in seconds !
        writeIntLE(shotTimeSeconds) // total time spent before performing the shot, in seconds !
        writeFloatLE(longestDrive) // longest drive !
        writeIntLE(pangyaShots) // pangya hit count, the pangya rate displayed is calculated by dividing this by the totals shots !
        writeIntLE(timeouts) // timeouts?
        writeIntLE(oobShots) // total shots that went out of bounds !
        writeIntLE(totalDistance) // total distance !
        writeIntLE(totalHoles) // total holes !
        writeIntLE(unfinishedHoles)
        writeIntLE(holeInOnes) // hole in ones !
        writeShortLE(bunkerShots) // total shots that landed in a bunker?
        writeIntLE(fairwayShots) // total fairway shots, for some reason this is divided by 'total holes' !
        writeIntLE(albatross) // albatross !
        writeIntLE(0) // ?
        writeIntLE(successfulPutts) // successful putts !
        writeFloatLE(longestPutt) // longest putt !
        writeFloatLE(longestChipIn) // longest chip in !
        writeIntLE(experience) // experience !
        writeByte(level) // level !
        writeLongLE(4456) // total pang earned?
        writeIntLE(totalScore) // total score (sum of all final scores, under par = negative values) !
        writeZero(5)
        writeByte(0)
        writeLongLE(0)
        writeLongLE(0)
        writeLongLE(0)
        writeLongLE(0)
        writeLongLE(0)
        writeLongLE(0)
        writeIntLE(gamesPlayed) // total games played (used for quit rate) !
        writeIntLE(0) // team hole?
        writeIntLE(1) // team win?
        writeIntLE(2) // team game?
        writeIntLE(0) // ladder point?
        writeIntLE(0) // ladder hole?
        writeIntLE(0) // ladder win?
        writeIntLE(0) // ladder lose?
        writeIntLE(0) // ladder draw?
        writeIntLE(gameComboCurrentStreak) // game combo current streak !
        writeIntLE(gameComboBestStreak) // game combo best streak !
        writeIntLE(gamesQuit) // total games quit (used for quit rate) !
        writeIntLE(pangWonInBattle) // total pangs won in pang battle !
        writeIntLE(0)
        writeIntLE(0)
        writeIntLE(0)
        writeIntLE(0)
        writeIntLE(0)
        writeZero(10)
        writeIntLE(0)
        writeZero(8)
    }
}