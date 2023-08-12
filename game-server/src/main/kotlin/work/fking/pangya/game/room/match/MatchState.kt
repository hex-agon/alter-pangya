package work.fking.pangya.game.room.match

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.Rand
import work.fking.pangya.game.room.Course
import work.fking.pangya.game.room.HoleMode
import work.fking.pangya.game.room.write
import java.time.Duration
import java.time.LocalDateTime

private const val MAX_HOLES = 18
private const val RAIN_CHANCE = 100 // 1 in x

data class MatchState(
    val course: Course,
    val holeMode: HoleMode,
    val holeCount: Int,
    val shotTime: Duration,
    val gameTime: Duration,
) {
    val startTime: LocalDateTime = LocalDateTime.now()
    val randSeed: Int = Rand.nextInt()
    val holes: List<Hole> = generateHoles(course)
}

fun generateHoles(course: Course): List<Hole> {
    var cloudy = false
    return List(MAX_HOLES) { idx ->
        val weather = if (cloudy) {
            cloudy = false
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
            holePosition = Rand.between(0, 2),
            wind = Rand.between(0, 8),
            windDirection = Rand.between(0, 360),
            weather = weather
        )
    }
}

data class Hole(
    val randomId: Int = Rand.nextInt(),
    val course: Course,
    val number: Int,
    val holePosition: Int,
    val wind: Int,
    val windDirection: Int,
    val weather: HoleWeather
)

fun ByteBuf.write(hole: Hole) {
    writeIntLE(hole.randomId)
    writeByte(hole.holePosition)
    write(hole.course)
    writeByte(hole.number)
}

enum class HoleWeather {
    CLEAR,
    CLOUDY,
    RAINING
}

fun ByteBuf.write(holeWeather: HoleWeather) {
    writeShortLE(holeWeather.ordinal)
}