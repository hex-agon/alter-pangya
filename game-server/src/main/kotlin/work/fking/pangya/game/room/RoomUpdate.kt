package work.fking.pangya.game.room

import java.time.Duration

sealed interface RoomUpdate

data class RoomNameChange(val name: String) : RoomUpdate
data class RoomPasswordChange(val password: String) : RoomUpdate
data class RoomTypeChange(val type: RoomType) : RoomUpdate
data class RoomCourseChange(val course: Course) : RoomUpdate
data class RoomHoleCountChange(val count: Int) : RoomUpdate
data class RoomHoleModeChange(val holeMode: HoleMode) : RoomUpdate
data class RoomShotTimeChange(val shotTime: Duration) : RoomUpdate
data class RoomPlayerCountChange(val playerCount: Int) : RoomUpdate
data class RoomGameTimeChange(val gameTime: Duration) : RoomUpdate
data class RoomArtifactChange(val artifactIffId: Int) : RoomUpdate
data class RoomNaturalWindChange(val naturalWind: Boolean) : RoomUpdate
data class RoomHoleRepeatHoleChange(val repeatingHole: Int) : RoomUpdate
data class RoomHoleRepeatFixedHoleChange(val fixedHole: Boolean) : RoomUpdate
