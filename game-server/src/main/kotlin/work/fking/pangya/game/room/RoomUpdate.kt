package work.fking.pangya.game.room

sealed interface RoomUpdate

data class RoomNameChange(val name: String) : RoomUpdate
data class RoomPasswordChange(val password: String) : RoomUpdate
data class RoomTypeChange(val type: RoomType) : RoomUpdate
data class RoomCourseChange(val course: Course) : RoomUpdate
data class RoomHoleCountChange(val count: Int) : RoomUpdate
data class RoomHoleModeChange(val holeMode: HoleMode) : RoomUpdate
data class RoomShotTimeChange(val shotTime: Int) : RoomUpdate
data class RoomPlayerCountChange(val playerCount: Int) : RoomUpdate
data class RoomGameTimeChange(val gameTime: Int) : RoomUpdate
data class RoomArtifactChange(val artifactIffId: Int) : RoomUpdate
data class RoomNaturalWindChange(val naturalWind: Boolean) : RoomUpdate