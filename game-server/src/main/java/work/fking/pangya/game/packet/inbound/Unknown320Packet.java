package work.fking.pangya.game.packet.inbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.PacketId;

public record Unknown320Packet() implements InboundPacket {

    @PacketId
    public static InboundPacket decode(ByteBuf buffer) {
        // sent when the shop is open
        return new Unknown320Packet();
    }
}
