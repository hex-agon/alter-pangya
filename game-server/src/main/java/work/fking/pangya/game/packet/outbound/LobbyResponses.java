package work.fking.pangya.game.packet.outbound;

import work.fking.pangya.networking.protocol.OutboundPacket;

public class LobbyResponses {

    public static OutboundPacket ackLeave() {
        return buffer -> buffer.writeShortLE(0xf6);
    }
}
