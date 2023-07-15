package work.fking.pangya.game.room.match

import work.fking.pangya.game.room.Room

interface MatchDirector {
    fun handleMatchEvent(room: Room, matchState: MatchState, event: MatchEvent)
    fun tick()
}

fun noopMatchDirector(): MatchDirector {
    return object : MatchDirector {
        override fun handleMatchEvent(room: Room, matchState: MatchState, event: MatchEvent) {
        }

        override fun tick() {
        }
    }
}