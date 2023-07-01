package work.fking.pangya.game.room

enum class RoomType(
    val id: Int,
    val uiType: Int = id, // I don't have a better name for this but this + id in the RoomInfo packet affects which UI is displayed
    val extendedInfo: Boolean = true // When true the player character info will be appended to specific room related packets
) {
    VERSUS(0),
    LOUNGE(2),
    TOURNAMENT(4),
    PANG_BATTLE(10),
    PRACTICE(19, uiType = 4, extendedInfo = false);

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
