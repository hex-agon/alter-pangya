package work.fking.pangya.game.model

enum class HoleMode {
    FRONT,
    BACK,
    RANDOM,
    SHUFFLE,
    REPEAT,
    SHUFFLE_COURSE;

    companion object {
        private val VALUES = values()
        fun forId(id: Byte): HoleMode? {
            return if (id < VALUES.size) {
                VALUES[id.toInt()]
            } else null
        }
    }
}
