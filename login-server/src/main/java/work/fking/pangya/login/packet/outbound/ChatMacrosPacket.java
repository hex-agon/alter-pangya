package work.fking.pangya.login.packet.outbound;

import io.netty.buffer.ByteBuf;
import io.netty.util.AttributeMap;
import work.fking.pangya.networking.protocol.OutboundPacket;
import work.fking.pangya.networking.protocol.ProtocolUtils;

public class ChatMacrosPacket implements OutboundPacket {

    private static final int ID = 6;

    @Override
    public void encode(ByteBuf buffer, AttributeMap attributeMap) {
        buffer.writeShortLE(ID);

        for (int i = 0; i < 9; i++) {
            ProtocolUtils.writeFixedSizeString(buffer, "Welcome to PangYa!", 64);
        }
    }
}
