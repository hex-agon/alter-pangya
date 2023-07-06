package work.fking.pangya.game.room

import work.fking.pangya.game.room.match.MatchDirector
import work.fking.pangya.game.room.match.PracticeMatchDirector
import work.fking.pangya.game.room.match.noopMatchDirector

enum class RoomType(
    val id: Int,
    val uiType: Int = id,
    val extendedInfo: Boolean = true, // I don't have a better name for this but this + id in the RoomInfo packet affects which UI is displayed
    val matchDirector: MatchDirector
) {
    VERSUS(0, matchDirector = noopMatchDirector()),
    LOUNGE(2, matchDirector = noopMatchDirector()),
    TOURNAMENT(4, matchDirector = noopMatchDirector()),
    PANG_BATTLE(10, matchDirector = noopMatchDirector()),
    PRACTICE(19, uiType = 4, extendedInfo = false, matchDirector = PracticeMatchDirector());

    companion object {
        fun forId(id: Byte): RoomType {
            return when (id.toInt()) {
                0 -> VERSUS
                2 -> LOUNGE
                4 -> TOURNAMENT
                10 -> PANG_BATTLE
                19 -> PRACTICE
                else -> VERSUS
            }
        }
    }
}
