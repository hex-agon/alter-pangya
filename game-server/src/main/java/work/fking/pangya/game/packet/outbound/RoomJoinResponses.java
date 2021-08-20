package work.fking.pangya.game.packet.outbound;

import work.fking.pangya.networking.protocol.OutboundPacket;
import work.fking.pangya.networking.protocol.ProtocolUtils;

public final class RoomJoinResponses {

    private static final int ID = 0x49;

    private static final int SUCCESS = 0;
    private static final int ALREADY_STARTED = 8;
    private static final int CANNOT_CREATE = 18;

    private RoomJoinResponses() {
    }

    public static OutboundPacket success(String name, int number) {
        return buffer -> {
            buffer.writeShortLE(ID);
            buffer.writeByte(SUCCESS);

            buffer.writeZero(1);
            ProtocolUtils.writePString(buffer, name);
            buffer.writeZero(25);
            buffer.writeShortLE(number);
            buffer.writeZero(111);
            buffer.writeIntLE(0); // eventNumber
            buffer.writeZero(12);
        };
    }

    public static OutboundPacket alreadyStarted() {
        return buffer -> {
            buffer.writeShortLE(ID);
            buffer.writeByte(ALREADY_STARTED);
        };
    }

    public static OutboundPacket cannotCreate() {
        return buffer -> {
            buffer.writeShortLE(ID);
            buffer.writeByte(CANNOT_CREATE);
        };
    }
}
