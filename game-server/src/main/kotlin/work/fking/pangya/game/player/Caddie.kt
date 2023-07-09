package work.fking.pangya.game.player

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.model.IffObject
import java.time.Instant
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit

@JvmRecord
data class Caddie(
    override val uid: Int,
    override val iffId: Int,
    private val levelsGained: Int,
    private val experience: Int
) : IffObject {

    override fun encode(buffer: ByteBuf) {
        buffer.writeIntLE(uid)
        buffer.writeIntLE(iffId)
        buffer.writeIntLE(0)
        buffer.writeByte(levelsGained)
        buffer.writeIntLE(experience)
        buffer.writeLongLE(0)
    }
}

fun nullCaddie() = Caddie(uid = 0, iffId = 0, levelsGained = 0, experience = 0)
