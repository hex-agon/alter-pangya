package work.fking.pangya.game

import work.fking.pangya.discovery.ServerBoost
import work.fking.pangya.discovery.ServerConfig
import work.fking.pangya.discovery.ServerFlag
import work.fking.pangya.discovery.ServerIcon

data class GameServerConfig(
    override val id: Int,
    override val name: String,
    override val capacity: Int,
    override val advertiseAddress: String,
    override val port: Int,
    override val flags: List<ServerFlag>?,
    override val boosts: List<ServerBoost>?,
    override val icon: ServerIcon?,
    val bindAddress: String,
    val serverChannels: List<ServerChannel> = emptyList(),
    val database: DatabaseConfig,
    val redis: RedisConfig
) : ServerConfig

data class DatabaseConfig(
    val url: String,
    val username: String,
    val password: String
)

data class RedisConfig(
    val url: String
)