package work.fking.pangya.game.room.match

import work.fking.pangya.common.Rand
import work.fking.pangya.game.packet.outbound.MatchReplies
import work.fking.pangya.game.room.Room

class PracticeMatchDirector : MatchDirector {

    override fun handleMatchEvent(room: Room, matchState: MatchState, event: MatchEvent) {
        when (event) {
            is PlayerFinishedPreviewEvent -> handleFinishedPreview(event)
            is PlayerHoleStartEvent -> handleHoleStart(matchState, event)
            is PlayerTourneyShotEvent -> handleTourneyShot(room, event)
            is PlayerShotSyncEvent -> handleShotSync(room, event)
            is PlayerUseItemEvent -> handleUseItem(room, event)
            is PlayerQuitEvent -> handlePlayerQuit(room, event)
            else -> return
        }
    }


    private fun handleFinishedPreview(event: PlayerFinishedPreviewEvent) {
        event.player.writeAndFlush(MatchReplies.gameFinishPlayerPreviewAck())
    }

    private fun handleHoleStart(matchState: MatchState, event: PlayerHoleStartEvent) {
        val hole = matchState.holes[event.hole - 1]

        with(event.player) {
            currentHole = event.hole
            write(MatchReplies.holeWeather(hole))
            write(MatchReplies.holeWind(hole))
            write(MatchReplies.gamePlayerStartHole(this))
            flush()
        }
    }

    private fun handleTourneyShot(room: Room, event: PlayerTourneyShotEvent) {
        val player = event.player
        room.broadcast(MatchReplies.gameTourneyShotAck(player, event.tourneyShotData))
    }

    private fun handleShotSync(room: Room, event: PlayerShotSyncEvent) {
        val player = event.player
        room.broadcast(
            MatchReplies.gameTourneyShotGhost(
                player = player,
                x = event.x,
                z = event.z,
                shotFlags = event.shotFlags,
                frames = event.frames
            )
        )
    }

    private fun handleUseItem(room: Room, event: PlayerUseItemEvent) {
        val randSeed = Rand.nextInt()
        room.broadcast(MatchReplies.gamePlayerUseItem(event.player, event.itemIffId, randSeed))
    }

    private fun handlePlayerQuit(room: Room, event: PlayerQuitEvent) {
        with(event.player) {
            write(MatchReplies.gameTourneyEndingScore())
            write(MatchReplies.gameTourneyWinnings())
            write(MatchReplies.gameTourneyTimeout())
        }
        room.broadcast(MatchReplies.gameTourneyUpdatePlayerProgress(event.player))
    }
}