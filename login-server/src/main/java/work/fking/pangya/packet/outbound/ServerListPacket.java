package work.fking.pangya.packet.outbound;

import io.netty.buffer.ByteBuf;
import io.netty.util.AttributeMap;
import work.fking.pangya.networking.protocol.OutboundPacket;
import work.fking.pangya.networking.protocol.ProtocolUtils;

public class ServerListPacket implements OutboundPacket {

    private static final int ID = 2;

    @Override
    public void encode(ByteBuf buffer, AttributeMap attributeMap) {
        buffer.writeShortLE(ID);
        buffer.writeByte(1); // server list size

        // repeat for each
        ProtocolUtils.writeFixedSizeString(buffer, "Testing", 40);
        buffer.writeIntLE(20202); // serverId
        buffer.writeIntLE(1000); // capacity
        buffer.writeIntLE(500); // playerCount
        ProtocolUtils.writeFixedSizeString(buffer, "127.0.0.1", 18); // serverIp
        buffer.writeShortLE(20202); // serverPort
        buffer.writeShortLE(0);
        buffer.writeShortLE(0); // flags?
        buffer.writeBytes(new byte[14]);
        buffer.writeShortLE(0); // flags?
    }
}
