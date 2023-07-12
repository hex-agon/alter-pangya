package work.fking.pangya.game.player

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.model.IffObject
import java.time.OffsetDateTime

data class PlayerAchievement(
    override val iffId: Int,
    override val uid: Int,
    val quests: List<PlayerQuest>
) : IffObject {

    override fun encode(buffer: ByteBuf) {
        with(buffer) {
            writeIntLE(iffId)
            writeIntLE(uid)

            writeIntLE(quests.size)
            quests.forEach { it.encode(buffer) }
        }
    }
}

data class PlayerQuest(
    val iffId: Int,
    val uid: Int,
    val progress: Int = 0,
    val completedAt: OffsetDateTime? = null
) {
    fun encode(buffer: ByteBuf) {
        buffer.writeIntLE(iffId)
        buffer.writeIntLE(progress)
        buffer.writeIntLE(completedAt?.toEpochSecond()?.toInt() ?: 0)
    }
}