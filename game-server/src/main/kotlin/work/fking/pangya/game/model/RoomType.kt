package work.fking.pangya.game.model

enum class RoomType(
    private val id: Int
) {
    VERSUS(0),
    CHAT(2),
    TOURNAMENT(4),
    PANG_BATTLE(10),
    PRACTICE(19);

    fun id(): Int {
        return id
    }

    companion object {
        fun forId(id: Byte): RoomType? {
            return when (id.toInt()) {
                0 -> VERSUS
                2 -> CHAT
                4 -> TOURNAMENT
                10 -> PANG_BATTLE
                19 -> PRACTICE
                else -> null
            }
        }
    }
}
