package work.fking.pangya.login.packet.outbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.discovery.ServerInfo;
import work.fking.pangya.networking.protocol.OutboundPacket;
import work.fking.pangya.networking.protocol.ProtocolUtils;

import java.util.List;

public class ServerListReplies {

    private static final int GAME_SERVERS_PACKET_ID = 0x2;
    private static final int SOCIAL_SERVERS_PACKET_ID = 0x9;

    public static OutboundPacket gameServers(List<ServerInfo> gameServers) {
        return buffer -> {
            buffer.writeShortLE(GAME_SERVERS_PACKET_ID);
            buffer.writeByte(gameServers.size());

            for (var server : gameServers) {
                encodeServerInfo(buffer, server);
            }
        };
    }

    public static OutboundPacket socialServers(List<ServerInfo> socialServers) {
        return buffer -> {
            buffer.writeShortLE(SOCIAL_SERVERS_PACKET_ID);
            buffer.writeByte(socialServers.size());

            for (var server : socialServers) {
                encodeServerInfo(buffer, server);
            }
        };
    }

    private static void encodeServerInfo(ByteBuf buffer, ServerInfo server) {
        int serverFlags = 0;

        for (var flag : server.flags()) {
            serverFlags |= flag.value();
        }
        int serverBoosts = 0;
        for (var boost : server.boosts()) {
            serverBoosts |= boost.value();
        }

        ProtocolUtils.writeFixedSizeString(buffer, server.name(), 40);
        buffer.writeIntLE(server.id());
        buffer.writeIntLE(server.capacity());
        buffer.writeIntLE(server.playerCount());
        ProtocolUtils.writeFixedSizeString(buffer, server.ip(), 18);
        buffer.writeShortLE(server.port());
        buffer.writeShortLE(0xFFFF); // unknown
        buffer.writeShortLE(serverFlags);
        buffer.writeIntLE(0xFFFFFFFF); // unknown
        buffer.writeShortLE(0xFFFF); // unknown
        buffer.writeByte(serverBoosts); // server boosts
        buffer.writeIntLE(0xFFFFFFFF); // unknown
        buffer.writeShortLE(0xFFFF); // unknown
        buffer.writeByte(0xFF); // unknown
        buffer.writeShortLE(server.icon().ordinal());
    }
}
