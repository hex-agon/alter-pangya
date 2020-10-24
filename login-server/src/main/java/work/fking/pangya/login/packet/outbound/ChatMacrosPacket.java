package work.fking.pangya.login.packet.outbound;

import io.netty.buffer.ByteBuf;
import io.netty.util.AttributeMap;
import work.fking.pangya.networking.protocol.OutboundPacket;

public class ChatMacrosPacket implements OutboundPacket {

    private static final int ID = 6;

    @Override
    public void encode(ByteBuf buffer, AttributeMap attributeMap) {
        buffer.writeShortLE(ID);
    }
}
