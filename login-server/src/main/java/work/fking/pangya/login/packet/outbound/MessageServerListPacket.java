package work.fking.pangya.login.packet.outbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.protocol.OutboundPacket;
import work.fking.pangya.networking.protocol.ProtocolUtils;

public class MessageServerListPacket implements OutboundPacket {

    private static final int ID = 9;

    @Override
    public void encode(ByteBuf target) {
        target.writeShortLE(ID);
        ProtocolUtils.writeFixedSizeString(target, "Testing", 40);
        target.writeIntLE(20302); // serverId
        target.writeIntLE(1000); // capacity
        target.writeIntLE(500); // playerCount
        ProtocolUtils.writeFixedSizeString(target, "127.0.0.1", 18); // serverIp
        target.writeShortLE(20302); // serverPort
        target.writeShortLE(0xFFFF); // unknown
    }
}
