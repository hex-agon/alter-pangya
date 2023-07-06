package work.fking.pangya.game.room.match

import io.netty.buffer.ByteBuf
import work.fking.pangya.common.Rand
import work.fking.pangya.game.model.Coord
import work.fking.pangya.game.room.Course
import work.fking.pangya.game.room.RoomPlayer
import java.time.LocalDateTime

private const val MAX_HOLES = 18

data class Match(
    val startTime: LocalDateTime = LocalDateTime.now(),
    val course: Course,
    val holes: List<Hole> = generateHoles(course)
)

fun generateHoles(course: Course): List<Hole> {
    return List(MAX_HOLES) { idx -> Hole(course = Course.BLUE_LAGOON, holeNumber = idx + 1, wind = Rand.between(1, 9), windDirection = Rand.between(0, 360)) }
}

data class Hole(val randomId: Int = Rand.nextInt(), val course: Course, val holeNumber: Int, val wind: Int, val windDirection: Int) {

    fun encode(buffer: ByteBuf) {
        buffer.writeIntLE(randomId)
        buffer.writeByte(0)
        buffer.writeByte(course.ordinal)
        buffer.writeByte(holeNumber)
    }
}

sealed interface MatchEvent

data class PlayerFinishedPreviewEvent(val player: RoomPlayer) : MatchEvent
data class PlayerHoleStartEvent(val player: RoomPlayer, val hole: Int, val par: Int, val teeCoord: Coord, val holeCoord: Coord) : MatchEvent
data class PlayerHoleFinishEvent(val player: RoomPlayer) : MatchEvent
data class PlayerRotateEvent(val player: RoomPlayer, val rotation: Float) : MatchEvent
data class PlayerShotCommitEvent(val player: RoomPlayer, val shotData: ShotData) : MatchEvent
data class PlayerShotStartEvent(val player: RoomPlayer) : MatchEvent
data class PlayerTurnEndEvent(val player: RoomPlayer) : MatchEvent