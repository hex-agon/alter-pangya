package work.fking.pangya.game.player

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.model.IffObject
import java.time.OffsetDateTime

data class PlayerAchievement(
    override val uid: Int,
    override val iffId: Int,
    val milestones: List<PlayerAchievementMilestone>
) : IffObject {

    override fun encode(buffer: ByteBuf) {
        with(buffer) {
            writeIntLE(iffId)
            writeIntLE(uid)

            writeIntLE(milestones.size)
            milestones.forEach { it.encode(buffer) }
        }
    }
}

data class PlayerAchievementMilestone(
    val uid: Int,
    val iffId: Int,
    val progress: Int = 0,
    val completedAt: OffsetDateTime? = null
) {
    fun encode(buffer: ByteBuf) {
        buffer.writeIntLE(iffId)
        buffer.writeIntLE(progress)
        buffer.writeIntLE(completedAt?.toEpochSecond()?.toInt() ?: 0)
    }
}