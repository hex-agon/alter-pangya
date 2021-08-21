package work.fking.pangya.login.packet.outbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.common.model.server.ServerBoost;
import work.fking.pangya.common.model.server.ServerFlag;
import work.fking.pangya.common.model.server.ServerIcon;
import work.fking.pangya.networking.protocol.OutboundPacket;
import work.fking.pangya.networking.protocol.ProtocolUtils;

import java.util.List;

public class ServerListPacket implements OutboundPacket {

    private static final int ID = 2;

    @Override
    public void encode(ByteBuf buffer) {

        buffer.writeShortLE(ID);
        buffer.writeByte(1); // size

        List<ServerFlag> flags = List.of();
        int serverFlags = 0;

        for (ServerFlag flag : flags) {
            serverFlags |= flag.value();
        }
        List<ServerBoost> boosts = List.of();
        int serverBoosts = 0;
        for (ServerBoost boost : boosts) {
            serverBoosts |= boost.value();
        }

        ProtocolUtils.writeFixedSizeString(buffer, "Black papel", 40);
        buffer.writeIntLE(20202);
        buffer.writeIntLE(1000);
        buffer.writeIntLE(10);
        ProtocolUtils.writeFixedSizeString(buffer, "127.0.0.1", 18);
        buffer.writeShortLE(20202);
        buffer.writeShortLE(0); // unknown
        buffer.writeShortLE(serverFlags);
        buffer.writeIntLE(0xFFFFFFFF); // unknown
        buffer.writeShortLE(0xFFFF); // unknown
        buffer.writeByte(serverBoosts); // server boosts
        buffer.writeIntLE(0xFFFFFFFF); // unknown
        buffer.writeShortLE(0xFFFF); // unknown
        buffer.writeByte(0xFF); // unknown
        buffer.writeShortLE(ServerIcon.BLACK_PAPEL.ordinal());
    }
}
