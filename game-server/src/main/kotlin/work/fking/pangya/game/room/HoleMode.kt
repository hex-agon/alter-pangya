package work.fking.pangya.game.room

import io.netty.buffer.ByteBuf

enum class HoleMode {
    FRONT,
    BACK,
    RANDOM,
    SHUFFLE,
    REPEAT,
    SHUFFLE_COURSE;

}

fun ByteBuf.write(holeMode: HoleMode) {
    writeByte(holeMode.ordinal)
}

fun holeModeById(id: Byte): HoleMode = HoleMode.entries.find { it.ordinal == id.toInt() } ?: HoleMode.FRONT
