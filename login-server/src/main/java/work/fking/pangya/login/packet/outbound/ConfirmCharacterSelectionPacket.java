package work.fking.pangya.login.packet.outbound;

import io.netty.buffer.ByteBuf;
import io.netty.util.AttributeMap;
import work.fking.pangya.networking.protocol.OutboundPacket;

public class ConfirmCharacterSelectionPacket implements OutboundPacket {

    private static final ConfirmCharacterSelectionPacket INSTANCE = new ConfirmCharacterSelectionPacket();
    private static final int ID = 17;

    private ConfirmCharacterSelectionPacket() {
    }

    @Override
    public void encode(ByteBuf buffer, AttributeMap attributeMap) {
        buffer.writeShortLE(ID);
        buffer.writeByte(0);
    }

    public static ConfirmCharacterSelectionPacket instance() {
        return INSTANCE;
    }
}
