package work.fking.pangya.game.model

import io.netty.buffer.ByteBuf
import java.util.concurrent.TimeUnit

class PlayerStatistic {

    fun encode(buffer: ByteBuf) {
        buffer.writeIntLE(100) // total shots !
        buffer.writeIntLE(2) // total putts !
        buffer.writeIntLE(TimeUnit.DAYS.toSeconds(1).toInt()) // total play time, in seconds !
        buffer.writeIntLE(60) // total time spent before performing the shot, in seconds !
        buffer.writeFloatLE(456.76f) // longest drive !
        buffer.writeIntLE(7) // pangya hit count, the pangya rate displayed is calculated by dividing this by the totals shots !
        buffer.writeIntLE(0) // timeouts?
        buffer.writeIntLE(0) // total shots that went out of bounds !
        buffer.writeIntLE(547899) // total distance !
        buffer.writeIntLE(10) // total holes !
        buffer.writeIntLE(0)
        buffer.writeIntLE(54) // hole in ones !
        buffer.writeShortLE(0) // total shots that landed in a bunker?
        buffer.writeIntLE(5) // total fairway shots, for some reason this is divided by 'total holes' !
        buffer.writeIntLE(33) // albatross !
        buffer.writeIntLE(0) // ?
        buffer.writeIntLE(1) // successful putts !
        buffer.writeFloatLE(33.7f) // longest putt !
        buffer.writeFloatLE(242.2f) // longest chip in !
        buffer.writeIntLE(346) // experience !
        buffer.writeByte(20) // level !
        buffer.writeLongLE(4456) // total pang earned?
        buffer.writeIntLE(-5) // total score (sum of all final scores, under par = negative values) !
        buffer.writeZero(5)
        buffer.writeByte(0)
        buffer.writeLongLE(0)
        buffer.writeLongLE(0)
        buffer.writeLongLE(0)
        buffer.writeLongLE(0)
        buffer.writeLongLE(0)
        buffer.writeLongLE(0)
        buffer.writeIntLE(51) // total games played (used for quit rate) !
        buffer.writeIntLE(0) // team hole?
        buffer.writeIntLE(1) // team win?
        buffer.writeIntLE(2) // team game?
        buffer.writeIntLE(0) // ladder point?
        buffer.writeIntLE(0) // ladder hole?
        buffer.writeIntLE(0) // ladder win?
        buffer.writeIntLE(0) // ladder lose?
        buffer.writeIntLE(0) // ladder draw?
        buffer.writeIntLE(1) // game combo current streak !
        buffer.writeIntLE(2) // game combo best streak !
        buffer.writeIntLE(3) // total games quit (used for quit rate) !
        buffer.writeIntLE(540) // total pangs won in pang battle !
        buffer.writeIntLE(0)
        buffer.writeIntLE(0)
        buffer.writeIntLE(0)
        buffer.writeIntLE(0)
        buffer.writeIntLE(0)
        buffer.writeZero(10)
        buffer.writeIntLE(0) // game count
        buffer.writeZero(8)
    }
}
