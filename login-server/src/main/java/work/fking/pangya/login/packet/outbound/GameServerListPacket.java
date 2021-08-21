package work.fking.pangya.login.packet.outbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.common.model.server.ServerBoost;
import work.fking.pangya.common.model.server.ServerFlag;
import work.fking.pangya.discovery.ServerInfo;
import work.fking.pangya.networking.protocol.OutboundPacket;
import work.fking.pangya.networking.protocol.ProtocolUtils;

import java.util.List;

public class GameServerListPacket implements OutboundPacket {

    private static final int ID = 2;

    private final List<ServerInfo> gameServers;

    private GameServerListPacket(List<ServerInfo> gameServers) {
        this.gameServers = gameServers;
    }

    public static GameServerListPacket create(List<ServerInfo> gameServers) {
        return new GameServerListPacket(gameServers);
    }

    @Override
    public void encode(ByteBuf buffer) {

        buffer.writeShortLE(ID);
        buffer.writeByte(gameServers.size()); // size

        for (ServerInfo server : gameServers) {
            int serverFlags = 0;

            for (ServerFlag flag : server.flags()) {
                serverFlags |= flag.value();
            }
            int serverBoosts = 0;
            for (ServerBoost boost : server.boosts()) {
                serverBoosts |= boost.value();
            }

            ProtocolUtils.writeFixedSizeString(buffer, server.name(), 40);
            buffer.writeIntLE(server.id());
            buffer.writeIntLE(server.capacity());
            buffer.writeIntLE(server.playerCount());
            ProtocolUtils.writeFixedSizeString(buffer, server.ip(), 18);
            buffer.writeShortLE(server.port());
            buffer.writeShortLE(0); // unknown
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
}
