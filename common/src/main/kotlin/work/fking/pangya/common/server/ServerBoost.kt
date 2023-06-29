package work.fking.pangya.common.server

enum class ServerBoost(
    val value: Int
) {
    DOUBLE_PANG(1 shl 1),
    DOUBLE_EXP(1 shl 2),
    ANGEL_EVENT(1 shl 3),
    TRIPLE_EXP(1 shl 4),
    CLUB_MASTERY(1 shl 7);
}
