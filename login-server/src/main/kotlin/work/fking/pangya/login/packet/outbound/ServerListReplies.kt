package work.fking.pangya.login.packet.outbound

import io.netty.buffer.ByteBuf
import work.fking.pangya.discovery.ServerInfo
import work.fking.pangya.networking.protocol.OutboundPacket
import work.fking.pangya.networking.protocol.writeFixedSizeString

object ServerListReplies {
    private const val GAME_SERVERS_PACKET_ID = 0x2
    private const val SOCIAL_SERVERS_PACKET_ID = 0x9

    fun gameServers(gameServers: List<ServerInfo>): OutboundPacket {
        return OutboundPacket { buffer: ByteBuf ->
            buffer.writeShortLE(GAME_SERVERS_PACKET_ID)
            buffer.writeByte(gameServers.size)
            for (server in gameServers) {
                encodeServerInfo(buffer, server)
            }
        }
    }

    fun socialServers(socialServers: List<ServerInfo>): OutboundPacket {
        return OutboundPacket { buffer: ByteBuf ->
            buffer.writeShortLE(SOCIAL_SERVERS_PACKET_ID)
            buffer.writeByte(socialServers.size)
            for (server in socialServers) {
                encodeServerInfo(buffer, server)
            }
        }
    }

    private fun encodeServerInfo(buffer: ByteBuf, server: ServerInfo) {
        var serverFlags = 0
        for (flag in server.flags) {
            serverFlags = serverFlags or flag.value
        }
        var serverBoosts = 0
        for (boost in server.boosts) {
            serverBoosts = serverBoosts or boost.value
        }
        buffer.writeFixedSizeString(server.name, 40)
        buffer.writeIntLE(server.id)
        buffer.writeIntLE(server.capacity)
        buffer.writeIntLE(server.playerCount)
        buffer.writeFixedSizeString(server.ip, 18)
        buffer.writeShortLE(server.port)
        buffer.writeShortLE(0xFFFF) // unknown
        buffer.writeShortLE(serverFlags)
        buffer.writeIntLE(-0x1) // unknown
        buffer.writeShortLE(0xFFFF) // unknown
        buffer.writeByte(serverBoosts) // server boosts
        buffer.writeIntLE(-0x1) // unknown
        buffer.writeShortLE(0xFFFF) // unknown
        buffer.writeByte(0xFF) // unknown
        buffer.writeShortLE(server.icon.ordinal)
    }
}
