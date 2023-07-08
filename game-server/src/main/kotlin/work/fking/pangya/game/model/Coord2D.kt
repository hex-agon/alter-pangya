package work.fking.pangya.game.model

import io.netty.buffer.ByteBuf

data class Coord2D(val x: Float, val z: Float) {

    constructor(buffer: ByteBuf) : this(
        x = buffer.readFloatLE(),
        z = buffer.readFloatLE()
    )
}

fun ByteBuf.write(coord: Coord2D) {
    writeFloatLE(coord.x)
    writeFloatLE(coord.z)
}
