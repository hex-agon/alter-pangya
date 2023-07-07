package work.fking.pangya.game.room.match

import work.fking.pangya.game.room.Room

/**
 * Responsible for managing the match with actions such as:
 * - Timing out players
 * - Choosing whose turn it is
 * - Advancing the hole
 * - Finishing the game
 *
 * Each roomType may have a different MatchDirector.
 * MatchDirectors must be stateless as they are shared per room.
 */
interface MatchDirector {
    fun handleMatchEvent(room: Room, matchState: MatchState, event: MatchEvent)
}

fun noopMatchDirector(): MatchDirector {
    return object : MatchDirector {
        override fun handleMatchEvent(room: Room, matchState: MatchState, event: MatchEvent) {
        }
    }
}