package work.fking.pangya.game.model;

import work.fking.pangya.common.Rand;

public enum Course {
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

    public static Course forId(int id) {
        var values = values();

        if (id < values.length) {
            return values[id];
        } else { // anything else or random (id == 127)
            return values[Rand.max(values.length)];
        }
    }
}
