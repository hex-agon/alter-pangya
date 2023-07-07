package work.fking.pangya.game.model

import io.netty.buffer.ByteBuf

data class Coord3D(val x: Float, val y: Float, val z: Float) {
    constructor(buffer: ByteBuf) : this(
        x = buffer.readFloatLE(),
        y = buffer.readFloatLE(),
        z = buffer.readFloatLE()
    )

}

fun ByteBuf.write(coord: Coord3D) {
    this.writeFloatLE(coord.x)
    this.writeFloatLE(coord.y)
    this.writeFloatLE(coord.z)
}
