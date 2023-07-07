package work.fking.pangya.game.room

import io.netty.buffer.ByteBuf
import work.fking.pangya.common.Rand

enum class Course {
    BLUE_LAGOON,
    BLUE_WATER,
    SEPIA_WIND,
    WIND_HILL,
    WIZ_WIZ,
    WEST_WIZ,
    BLUE_MOON,
    SILVIA_CANNON,
    ICE_CANNON,
    WHITE_WIZ,
    SHINING_SAND,
    PINK_WIND,
    UNKNOWN_12,
    DEEP_INFERNO,
    ICE_SPA,
    LOST_SEAWAY,
    EASTERN_VALLEY,
    UNKNOWN_17,
    ICE_INFERNO,
    WIZ_CITY,
    ABBOT_MINE;

}

fun ByteBuf.write(course: Course) {
    this.writeByte(course.ordinal)
}

fun courseById(id: Byte): Course = Course.entries.find { it.ordinal == id.toInt() } ?: Course.entries[Rand.max(Course.entries.size)]
