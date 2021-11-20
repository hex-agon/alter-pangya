package work.fking.pangya.game.packet.inbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.game.packet.handler.Unknown156PacketHandler;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.Packet;

@Packet(id = 0x9c, handledBy = Unknown156PacketHandler.class)
public record Unknown156Packet() implements InboundPacket {

    public static InboundPacket decode(ByteBuf buffer) {
        return new Unknown156Packet();
    }
}
