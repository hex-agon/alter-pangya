package work.fking.pangya.game.player

import io.netty.buffer.ByteBuf
import java.util.concurrent.TimeUnit

data class PlayerStatistic(
    val totalShots: Int = 0,
    val totalPutts: Int = 0,
    val playtimeSeconds: Int = 0,
    val totalShotTimeSeconds: Int = 0,
    val longestDrive: Float = 0f,
    val totalPangyaShots: Int = 0,
    val totalOobs: Int = 0,
    val totalDistance: Int = 0,
    val totalHoles: Int = 0,
    val holeInOnes: Int = 0,
    val totalFairwayShots: Int = 0,
    val albatross: Int = 0,
    val successfulPutts: Int = 0,
    val longestPutt: Float = 0f,
    val longestChipIn: Float = 0f,
    val experience: Int = 0,
    val level: Int = 0,
    val totalScore: Int = 0,
    val totalGamesPlayed: Int = 0,
    val gameComboCurrentStreak: Int = 0,
    val gameComboBestStreak: Int = 0,
    val totalGamesQuit: Int = 0,
    val pangWonInBattle: Int = 0,
)

fun ByteBuf.write(playerStatistic: PlayerStatistic) {
    with(playerStatistic) {
        writeIntLE(100) // total shots !
        writeIntLE(2) // total putts !
        writeIntLE(TimeUnit.DAYS.toSeconds(1).toInt()) // total play time, in seconds !
        writeIntLE(60) // total time spent before performing the shot, in seconds !
        writeFloatLE(456.76f) // longest drive !
        writeIntLE(7) // pangya hit count, the pangya rate displayed is calculated by dividing this by the totals shots !
        writeIntLE(0) // timeouts?
        writeIntLE(0) // total shots that went out of bounds !
        writeIntLE(547899) // total distance !
        writeIntLE(10) // total holes !
        writeIntLE(0)
        writeIntLE(54) // hole in ones !
        writeShortLE(0) // total shots that landed in a bunker?
        writeIntLE(5) // total fairway shots, for some reason this is divided by 'total holes' !
        writeIntLE(33) // albatross !
        writeIntLE(0) // ?
        writeIntLE(1) // successful putts !
        writeFloatLE(33.7f) // longest putt !
        writeFloatLE(242.2f) // longest chip in !
        writeIntLE(24) // experience !
        writeByte(70) // level !
        writeLongLE(4456) // total pang earned?
        writeIntLE(-5) // total score (sum of all final scores, under par = negative values) !
        writeZero(5)
        writeByte(0)
        writeLongLE(0)
        writeLongLE(0)
        writeLongLE(0)
        writeLongLE(0)
        writeLongLE(0)
        writeLongLE(0)
        writeIntLE(51) // total games played (used for quit rate) !
        writeIntLE(0) // team hole?
        writeIntLE(1) // team win?
        writeIntLE(2) // team game?
        writeIntLE(0) // ladder point?
        writeIntLE(0) // ladder hole?
        writeIntLE(0) // ladder win?
        writeIntLE(0) // ladder lose?
        writeIntLE(0) // ladder draw?
        writeIntLE(1) // game combo current streak !
        writeIntLE(2) // game combo best streak !
        writeIntLE(3) // total games quit (used for quit rate) !
        writeIntLE(540) // total pangs won in pang battle !
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