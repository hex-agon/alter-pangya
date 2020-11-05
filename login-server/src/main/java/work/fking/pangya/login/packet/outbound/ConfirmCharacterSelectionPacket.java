package work.fking.pangya.login.packet.outbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.protocol.OutboundPacket;

public class ConfirmCharacterSelectionPacket implements OutboundPacket {

    private static final ConfirmCharacterSelectionPacket INSTANCE = new ConfirmCharacterSelectionPacket();
    private static final int ID = 17;

    private ConfirmCharacterSelectionPacket() {
    }

    public static ConfirmCharacterSelectionPacket instance() {
        return INSTANCE;
    }

    @Override
    public void encode(ByteBuf target) {
        target.writeShortLE(ID);
        target.writeByte(0);
    }
}
