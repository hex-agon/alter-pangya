package work.fking.pangya.game.model

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

    companion object {
        fun forId(id: Byte): Course {
            val values = values()
            return if (id < values.size) {
                values[id.toInt()]
            } else { // anything else or random (id == 127)
                values[Rand.max(values.size)]
            }
        }
    }
}
