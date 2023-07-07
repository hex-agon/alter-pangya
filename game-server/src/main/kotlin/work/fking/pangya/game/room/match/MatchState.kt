package work.fking.pangya.game.room.match

import io.netty.buffer.ByteBuf
import work.fking.pangya.common.Rand
import work.fking.pangya.game.room.Course
import work.fking.pangya.game.room.HoleMode
import work.fking.pangya.game.room.write
import java.time.LocalDateTime

private const val MAX_HOLES = 18
private const val RAIN_CHANCE = 100 // 1 in x

data class MatchState(
    val startTime: LocalDateTime = LocalDateTime.now(),
    val course: Course,
    val holeMode: HoleMode,
    val holeCount: Int,
    val shotTimeMs: Int,
    val gameTimeMs: Int,
    val holes: List<Hole> = generateHoles(course)
)

fun generateHoles(course: Course): List<Hole> {
    var cloudy = false
    return List(MAX_HOLES) { idx ->
        val weather = if (cloudy) {
            HoleWeather.RAINING
        } else if (Rand.max(RAIN_CHANCE) == 0) {
            cloudy = true
            HoleWeather.CLOUDY
        } else {
            HoleWeather.CLEAR
        }

        Hole(
            course = course,
            number = idx + 1,
            wind = Rand.between(1, 9),
            windDirection = Rand.between(0, 360),
            weather = weather
        )
    }
}

data class Hole(
    val randomId: Int = Rand.nextInt(),
    val course: Course,
    val number: Int,
    val wind: Int,
    val windDirection: Int,
    val weather: HoleWeather
)

fun ByteBuf.write(hole: Hole) {
    this.writeIntLE(hole.randomId)
    this.writeByte(0)
    this.write(hole.course)
    this.writeByte(hole.number)
}

enum class HoleWeather {
    CLEAR,
    CLOUDY,
    RAINING
}

fun ByteBuf.write(holeWeather: HoleWeather) {
    this.writeShortLE(holeWeather.ordinal)
}