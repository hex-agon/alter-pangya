package work.fking.pangya.login.packet.outbound;

import work.fking.pangya.networking.protocol.OutboundPacket;

public final class ConfirmCharacterReplies {

    private static final int PACKET_ID = 0x11;

    private ConfirmCharacterReplies() {
    }

    public static OutboundPacket ok() {
        return buffer -> {
            buffer.writeShortLE(PACKET_ID);
            buffer.writeByte(0);
        };
    }
}
