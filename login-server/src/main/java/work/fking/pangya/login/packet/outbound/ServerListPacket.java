package work.fking.pangya.login.packet.outbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.common.model.server.GameServer;
import work.fking.pangya.common.model.server.GameServerIcon;
import work.fking.pangya.networking.protocol.OutboundPacket;

import java.util.List;

public class ServerListPacket implements OutboundPacket {

    private static final int ID = 2;

    @Override
    public void encode(ByteBuf target) {

        target.writeShortLE(ID);
        List<GameServer> servers = List.of(
                GameServer.of(
                        20202,
                        "Black papel",
                        100,
                        10,
                        "127.0.0.1",
                        20202,
                        List.of(),
                        List.of(),
                        GameServerIcon.BLACK_PAPEL
                )
        );
        target.writeByte(servers.size());

        for (GameServer server : servers) {
            server.encode(target);
        }
    }
}
