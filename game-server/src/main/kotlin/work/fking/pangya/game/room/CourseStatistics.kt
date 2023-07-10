package work.fking.pangya.game.room

import io.netty.buffer.ByteBuf

class CourseStatistics(
    private val course: Course
) {
    fun serialize(buffer: ByteBuf) {
        with(buffer) {
            writeByte(course.ordinal) // courseId
            writeIntLE(5) // totalStrokes
            writeIntLE(4) // totalPutts
            writeIntLE(6) // holesPlayed
            writeIntLE(1) // total fairway shots
            writeIntLE(0) // holeInOnes
            writeIntLE(0)
            writeIntLE(-6 + course.ordinal) // totalScore
            writeByte(-3 + course.ordinal) // bestScore
            writeLongLE((654 + course.ordinal * 70).toLong()) // bestPangEarned
            writeIntLE(67108872) // bestScoreWithCharIffId
            writeByte(0)
        }
    }
}
