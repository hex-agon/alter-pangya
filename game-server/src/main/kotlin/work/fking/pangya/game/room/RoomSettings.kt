package work.fking.pangya.game.room

import io.netty.buffer.ByteBuf
import work.fking.pangya.networking.protocol.writePString

class RoomSettings(
    name: String,
    password: String? = null,
    roomType: RoomType,
    course: Course,
    holeMode: HoleMode,
    holeCount: Int,
    maxPlayers: Int,
    shotTime: Int,
    gameTime: Int,
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
    var maxPlayers = maxPlayers
        private set
    var shotTimeMs = shotTime
        private set
    var gameTimeMs = gameTime
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
            is RoomShotTimeChange -> shotTimeMs = update.shotTime
            is RoomPlayerCountChange -> maxPlayers = update.playerCount
            is RoomGameTimeChange -> gameTimeMs = update.gameTime
            is RoomArtifactChange -> artifactIffId = update.artifactIffId
            is RoomNaturalWindChange -> naturalWind = update.naturalWind
        }
    }

    fun encode(buffer: ByteBuf) {
        buffer.writeByte(type.uiType)
        buffer.writeByte(course.ordinal)
        buffer.writeByte(holeCount)
        buffer.writeByte(holeMode.ordinal)
        buffer.writeIntLE(if (naturalWind) 1 else 0)
        buffer.writeByte(maxPlayers) // max players
        buffer.writeByte(30)
        buffer.writeByte(0)
        buffer.writeIntLE(shotTimeMs)
        buffer.writeIntLE(gameTimeMs)
        buffer.writeIntLE(trophyIffId())
        buffer.writeByte(if (password != null) 1 else 0)
        buffer.writePString(name)
    }

    fun trophyIffId(): Int {
        return 0
    }
}