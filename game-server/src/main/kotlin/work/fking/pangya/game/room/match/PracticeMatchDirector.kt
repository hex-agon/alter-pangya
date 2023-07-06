package work.fking.pangya.game.room.match

import work.fking.pangya.game.packet.outbound.MatchReplies

class PracticeMatchDirector : MatchDirector {

    override fun handleMatchEvent(event: MatchEvent) {
        when (event) {
            is PlayerFinishedPreviewEvent -> handlePlayerFinishedPreviewEvent(event)
            is PlayerHoleStartEvent -> handlePlayerHoleStart(event)
            is PlayerHoleFinishEvent -> handlePlayerHoleFinish(event)
            is PlayerShotCommitEvent -> handlePlayerShotCommitEvent(event)
            is PlayerTurnEndEvent -> handlePlayerTurnEndEvent(event)
            else -> return
        }
    }

    private fun handlePlayerFinishedPreviewEvent(event: PlayerFinishedPreviewEvent) {
        event.player.writeAndFlush(MatchReplies.gameFinishPlayerPreviewAck())
    }

    private fun handlePlayerHoleStart(event: PlayerHoleStartEvent) {
        with(event.player) {
            finishedHole = false
            write(MatchReplies.gameHoleWeather())
            write(MatchReplies.gameHoleWind())
            write(MatchReplies.gamePlayerStartHole(this))
            flush()
        }
    }

    private fun handlePlayerHoleFinish(event: PlayerHoleFinishEvent) {
        event.player.finishedHole = true
    }

    private fun handlePlayerShotCommitEvent(event: PlayerShotCommitEvent) {
        val player = event.player
        player.writeAndFlush(MatchReplies.gamePlayerShotCommit(player, event.shotData))
    }

    private fun handlePlayerTurnEndEvent(event: PlayerTurnEndEvent) {
        val player = event.player
        player.write(MatchReplies.gamePlayerTurnEnd(player))

        if (player.finishedHole) {
            player.writeAndFlush(MatchReplies.gameFinishHole())
        } else {
            player.writeAndFlush(MatchReplies.gamePlayerTurnStart(player))
        }
    }
}