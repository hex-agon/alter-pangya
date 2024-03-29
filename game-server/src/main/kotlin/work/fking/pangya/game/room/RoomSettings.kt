package work.fking.pangya.game.room

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.room.HoleMode.REPEAT
import work.fking.pangya.networking.protocol.writePString
import java.time.Duration

class RoomSettings(
    name: String,
    password: String? = null,
    roomType: RoomType,
    course: Course,
    holeMode: HoleMode,
    repeatFixedHole: Boolean,
    repeatingHole: Int,
    holeCount: Int,
    maxPlayers: Int,
    shotTime: Duration,
    gameTime: Duration,
    artifactIffId: Int,
    naturalWind: Boolean
) {
    var name = name
        private set
    var password = password
        private set
    var type = roomType
        private set
    var course = course
        private set
    var holeCount = holeCount
        private set
    var holeMode = holeMode
        private set
    var repeatFixedHole = repeatFixedHole
        private set
    var repeatingHole = repeatingHole
        private set
    var maxPlayers = maxPlayers
        private set
    var shotTime = shotTime
        private set
    var gameTime = gameTime
        private set
    var artifactIffId = artifactIffId
        private set
    var naturalWind = naturalWind
        private set

    fun handleUpdate(update: RoomUpdate) {
        when (update) {
            is RoomNameChange -> name = update.name
            is RoomPasswordChange -> password = update.password
            is RoomTypeChange -> type = update.type
            is RoomCourseChange -> course = update.course
            is RoomHoleCountChange -> holeCount = update.count
            is RoomHoleModeChange -> holeMode = update.holeMode
            is RoomShotTimeChange -> shotTime = update.shotTime
            is RoomPlayerCountChange -> maxPlayers = update.playerCount
            is RoomGameTimeChange -> gameTime = update.gameTime
            is RoomArtifactChange -> artifactIffId = update.artifactIffId
            is RoomNaturalWindChange -> naturalWind = update.naturalWind
            is RoomHoleRepeatHoleChange -> repeatingHole = update.repeatingHole
            is RoomHoleRepeatFixedHoleChange -> repeatFixedHole = update.fixedHole
        }
    }

    fun trophyIffId(): Int {
        return 0
    }
}

fun ByteBuf.write(settings: RoomSettings) {
    with(settings) {
        writeByte(type.uiType)
        write(course)
        writeByte(holeCount)
        write(holeMode)
        if (holeMode == REPEAT) {
            writeByte(repeatingHole)
            writeIntLE(if (repeatFixedHole) 7 else 6)
        }
        writeIntLE(if (naturalWind) 1 else 0)
        writeByte(maxPlayers) // max players
        writeByte(30)
        writeByte(0)
        writeIntLE(shotTime.toMillis().toInt())
        writeIntLE(gameTime.toMillis().toInt())
        writeIntLE(trophyIffId())
        writeByte(if (password != null) 1 else 0)
        writePString(name)
    }
}