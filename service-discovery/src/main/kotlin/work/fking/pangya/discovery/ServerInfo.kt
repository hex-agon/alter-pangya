package work.fking.pangya.discovery

import work.fking.pangya.common.server.ServerBoost
import work.fking.pangya.common.server.ServerFlag
import work.fking.pangya.common.server.ServerIcon

@JvmRecord
data class ServerInfo(
    val type: ServerType,
    val id: Int,
    val name: String,
    val capacity: Int,
    val playerCount: Int,
    val ip: String,
    val port: Int,
    val flags: List<ServerFlag>,
    val boosts: List<ServerBoost>,
    val icon: ServerIcon
)

enum class ServerType {
    LOGIN,
    GAME,
    SOCIAL
}
