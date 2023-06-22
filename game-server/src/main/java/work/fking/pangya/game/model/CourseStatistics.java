package work.fking.pangya.game.model;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.game.packet.handler.room.CreateRoomPacketHandler.Course;

public class CourseStatistics {

    private final Course course;

    private CourseStatistics(Course course) {
        this.course = course;
    }

    public static CourseStatistics blank(Course course) {
        return new CourseStatistics(course);
    }

    public void serialize(ByteBuf buffer) {
        buffer.writeByte(course.ordinal()); // courseId
        buffer.writeIntLE(5); // totalStrokes
        buffer.writeIntLE(4); // totalPutts
        buffer.writeIntLE(6); // holesPlayed
        buffer.writeIntLE(1); // total fairway shots
        buffer.writeIntLE(0); // holeInOnes
        buffer.writeIntLE(0);
        buffer.writeIntLE(-6 + course.ordinal()); // totalScore
        buffer.writeByte(-3 + course.ordinal()); // bestScore
        buffer.writeLongLE(654 + course.ordinal() * 70); // bestPangEarned
        buffer.writeIntLE(67108872); // bestScoreWithCharIffId
        buffer.writeByte(0);
    }
}
