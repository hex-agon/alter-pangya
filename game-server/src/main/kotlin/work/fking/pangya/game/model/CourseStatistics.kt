package work.fking.pangya.game.model

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.room.Course

class CourseStatistics(
    private val course: Course
) {
    fun serialize(buffer: ByteBuf) {
        buffer.writeByte(course.ordinal) // courseId
        buffer.writeIntLE(5) // totalStrokes
        buffer.writeIntLE(4) // totalPutts
        buffer.writeIntLE(6) // holesPlayed
        buffer.writeIntLE(1) // total fairway shots
        buffer.writeIntLE(0) // holeInOnes
        buffer.writeIntLE(0)
        buffer.writeIntLE(-6 + course.ordinal) // totalScore
        buffer.writeByte(-3 + course.ordinal) // bestScore
        buffer.writeLongLE((654 + course.ordinal * 70).toLong()) // bestPangEarned
        buffer.writeIntLE(67108872) // bestScoreWithCharIffId
        buffer.writeByte(0)
    }
}
