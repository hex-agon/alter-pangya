package work.fking.pangya.login.packet.outbound;

import io.netty.buffer.ByteBuf;
import io.netty.util.AttributeMap;
import work.fking.pangya.networking.protocol.OutboundPacket;
import work.fking.pangya.networking.protocol.ProtocolUtils;

public class MessageServerListPacket implements OutboundPacket {

    private static final int ID = 9;

    @Override
    public void encode(ByteBuf buffer, AttributeMap attributeMap) {
        buffer.writeShortLE(ID);
        ProtocolUtils.writeFixedSizeString(buffer, "Testing", 40);
        buffer.writeIntLE(20202); // serverId
        buffer.writeIntLE(1000); // capacity
        buffer.writeIntLE(500); // playerCount
        ProtocolUtils.writeFixedSizeString(buffer, "127.0.0.1", 18); // serverIp
        buffer.writeShortLE(20202); // serverPort
        buffer.writeShortLE(0xFFFF); // unknown
    }
}
