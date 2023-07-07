package work.fking.pangya.game.room.match

import io.netty.buffer.ByteBuf

data class ShotCommitData(
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
        with(buffer) {
            writeFloatLE(maxClick)
            writeFloatLE(minClick)
            writeFloatLE(curve)
            writeFloatLE(spin)
            writeByte(accuracy)
            writeIntLE(special)
            writeIntLE(frame)
            writeFloatLE(shotAngle)
            writeIntLE(shotTime)
            writeFloatLE(center)
            writeByte(clubIndex)
            writeFloatLE(randomPower)
            writeFloatLE(randomAngle)
            writeFloatLE(impactWidth)
            writeFloatLE(windPower)
            writeFloatLE(windDirection)
        }
    }
}