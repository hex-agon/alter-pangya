package work.fking.pangya.game.room.match

import work.fking.pangya.game.packet.outbound.MatchReplies
import work.fking.pangya.game.room.Room

class PracticeMatchDirector : MatchDirector {

    override fun handleMatchEvent(room: Room, matchState: MatchState, event: MatchEvent) {
        when (event) {
            is PlayerFinishedPreviewEvent -> handleFinishedPreview(event)
            is PlayerHoleStartEvent -> handleHoleStart(matchState, event)
            is PlayerHoleFinishEvent -> handleHoleFinish(event)
            is PlayerTourneyShotEvent -> handleTourneyShot(room, event)
            is PlayerShotSyncEvent -> handleShotSync(room, event)
            is PlayerUseItemEvent -> handleUseItem(room, matchState, event)
            is PlayerQuitEvent -> handlePlayerQuit(room, event)
            else -> return
        }
    }


    private fun handleFinishedPreview(event: PlayerFinishedPreviewEvent) {
        event.player.writeAndFlush(MatchReplies.finishPlayerPreviewAck())
    }

    private fun handleHoleStart(matchState: MatchState, event: PlayerHoleStartEvent) {
        val hole = matchState.holes[event.hole - 1]

        with(event.player) {
            currentHole = event.hole
            write(MatchReplies.holeWeather(hole))
            write(MatchReplies.holeWind(hole))
            write(MatchReplies.playerStartHole(this))
            flush()
        }
    }

    private fun handleHoleFinish(event: PlayerHoleFinishEvent) {
        event.player.statistics = event.statistics
    }

    private fun handleTourneyShot(room: Room, event: PlayerTourneyShotEvent) {
        val player = event.player
        room.broadcast(MatchReplies.tourneyShotAck(player, event.tourneyShotData))
    }

    private fun handleShotSync(room: Room, event: PlayerShotSyncEvent) {
        val player = event.player
        player.pang = event.pang.toLong()
        player.bonusPang = event.bonusPang.toLong()
        room.broadcast(
            MatchReplies.tourneyShotGhost(
                player = player,
                x = event.x,
                z = event.z,
                shotFlags = event.shotFlags,
                frames = event.frames
            )
        )
    }

    private fun handleUseItem(room: Room, matchState: MatchState, event: PlayerUseItemEvent) {
        room.broadcast(MatchReplies.playerUseItem(event.player, event.itemIffId, matchState.randSeed))
    }

    private fun handlePlayerQuit(room: Room, event: PlayerQuitEvent) {
        with(event.player) {
            write(MatchReplies.tourneyEndingScore())
            write(MatchReplies.tourneyWinnings(listOf()))
            write(MatchReplies.tourneyTimeout())
        }
        room.broadcast(MatchReplies.tourneyUpdatePlayerProgress(event.player))
    }
}