package work.fking.pangya.game.room.match

import work.fking.pangya.game.model.Coord2D
import work.fking.pangya.game.player.PlayerStatistics
import work.fking.pangya.game.room.RoomPlayer

sealed interface MatchEvent

data class PlayerFinishedPreviewEvent(val player: RoomPlayer) : MatchEvent
data class PlayerHoleStartEvent(val player: RoomPlayer, val hole: Int, val par: Int, val teeCoord: Coord2D, val holeCoord: Coord2D) : MatchEvent
data class PlayerHoleFinishEvent(val player: RoomPlayer, val statistics: PlayerStatistics) : MatchEvent
data class PlayerRotateEvent(val player: RoomPlayer, val rotation: Float) : MatchEvent
data class PlayerShotCommitEvent(val player: RoomPlayer, val shotData: ShotCommitData) : MatchEvent
data class PlayerShotStartEvent(val player: RoomPlayer) : MatchEvent
data class PlayerTurnEndEvent(val player: RoomPlayer) : MatchEvent
data class PlayerTourneyShotEvent(val player: RoomPlayer, val tourneyShotData: TourneyShotData) : MatchEvent
data class PlayerShotSyncEvent(val player: RoomPlayer, val x: Float, val z: Float, val pang: Int, val bonusPang: Int, val shotFlags: Int, val frames: Int) : MatchEvent
data class PlayerUseItemEvent(val player: RoomPlayer, val itemIffId: Int) : MatchEvent
data class PlayerQuitEvent(val player: RoomPlayer) : MatchEvent
data class PlayerMatchFinishEvent(val player: RoomPlayer, val statistics: PlayerStatistics) : MatchEvent