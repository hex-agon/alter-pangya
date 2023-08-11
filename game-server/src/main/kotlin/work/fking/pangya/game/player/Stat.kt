package work.fking.pangya.game.player

enum class Stat {
    POWER,
    CONTROL,
    ACCURACY,
    SPIN,
    CURVE
}

fun statById(id: Int) = Stat.entries.find { it.ordinal == id }