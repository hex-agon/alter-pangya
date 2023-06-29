package work.fking.pangya.common.server

@JvmRecord
data class ServerConfig(
    val id: Int,
    val name: String,
    val capacity: Int,
    val bindAddress: String,
    val advertiseAddress: String,
    val port: Int,
    val flags: List<ServerFlag> = emptyList(),
    val boosts: List<ServerBoost> = emptyList(),
    val icon: ServerIcon = ServerIcon.BLACK_PAPEL
)
