package work.fking.pangya.game.room.match

import io.netty.buffer.ByteBuf

data class ShotData(
    val maxClick: Float,
    val minClick: Float,
    val curve: Float,
    val spin: Float,
    val accuracy: Int,
    val special: Int,
    val frame: Int, // the current client frame, resets per hole
    val shotAngle: Float,
    val shotTime: Int, // how long the player took to take the shot
    val center: Float,
    val clubIndex: Int,
    val randomPower: Float,
    val randomAngle: Float,
    val impactWidth: Float,
    val windPower: Float,
    val windDirection: Float,
) {
    fun encode(buffer: ByteBuf) {
        buffer.writeFloatLE(maxClick)
        buffer.writeFloatLE(minClick)
        buffer.writeFloatLE(curve)
        buffer.writeFloatLE(spin)
        buffer.writeByte(accuracy)
        buffer.writeIntLE(special)
        buffer.writeIntLE(frame)
        buffer.writeFloatLE(shotAngle)
        buffer.writeIntLE(shotTime)
        buffer.writeFloatLE(center)
        buffer.writeByte(clubIndex)
        buffer.writeFloatLE(randomPower)
        buffer.writeFloatLE(randomAngle)
        buffer.writeFloatLE(impactWidth)
        buffer.writeFloatLE(windPower)
        buffer.writeFloatLE(windDirection)
    }
}