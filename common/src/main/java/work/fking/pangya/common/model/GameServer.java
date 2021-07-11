package work.fking.pangya.common.model;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.protocol.ProtocolUtils;

import java.util.List;

public record GameServer(int id, String name, int capacity, int playerCount, String ip, int port, List<GameServerFlag> flags, List<GameServerBoost> boosts, GameServerIcon icon) {

    public static GameServer of(int id, String name, int capacity, int playerCount, String ip, int port, List<GameServerFlag> flags, List<GameServerBoost> boosts, GameServerIcon icon) {
        return new GameServer(id, name, capacity, playerCount, ip, port, flags, boosts, icon);
    }

    public void encode(ByteBuf buffer) {
        int serverFlags = 0;
        for (GameServerFlag flag : flags) {
            serverFlags |= flag.value();
        }

        int serverBoosts = 0;
        for (GameServerBoost boost : boosts) {
            serverBoosts |= boost.value();
        }

        ProtocolUtils.writeFixedSizeString(buffer, name, 40);
        buffer.writeIntLE(id);
        buffer.writeIntLE(capacity);
        buffer.writeIntLE(playerCount);
        ProtocolUtils.writeFixedSizeString(buffer, ip, 18);
        buffer.writeShortLE(port);
        buffer.writeShortLE(0); // unknown
        buffer.writeShortLE(serverFlags);
        buffer.writeIntLE(0xFFFFFFFF); // unknown
        buffer.writeShortLE(0xFFFF); // unknown
        buffer.writeByte(serverBoosts); // server boosts
        buffer.writeIntLE(0xFFFFFFFF); // unknown
        buffer.writeShortLE(0xFFFF); // unknown
        buffer.writeByte(0xFF); // unknown
        buffer.writeShortLE(icon.ordinal());
    }
}
