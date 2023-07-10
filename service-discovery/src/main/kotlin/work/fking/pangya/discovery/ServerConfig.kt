package work.fking.pangya.discovery

interface ServerConfig {
    val id: Int
    val name: String
    val capacity: Int
    val advertiseAddress: String
    val port: Int
    val flags: List<ServerFlag>?
    val boosts: List<ServerBoost>?
    val icon: ServerIcon?
}

enum class ServerFlag(
    val value: Int
) {
    HIDDEN(1 shl 4),
    SORT_PRIORITY(1 shl 7),
    GRAND_PRIX(1 shl 11);
}

enum class ServerBoost(
    val value: Int
) {
    DOUBLE_PANG(1 shl 1),
    DOUBLE_EXP(1 shl 2),
    ANGEL_EVENT(1 shl 3),
    TRIPLE_EXP(1 shl 4),
    CLUB_MASTERY(1 shl 7);
}

enum class ServerIcon {
    BLACK_PAPEL,
    PIPPIN,
    TITAN_BOO,
    DOLFINI,
    LOLO,
    QUMA,
    TIKI,
    CADIE,
    CIEN
}