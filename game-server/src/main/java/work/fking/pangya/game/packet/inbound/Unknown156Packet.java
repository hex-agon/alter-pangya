package work.fking.pangya.game.packet.inbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.game.packet.handler.Unknown156PacketHandler;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.PacketHandledBy;
import work.fking.pangya.networking.protocol.PacketId;

@PacketId(0x9c)
@PacketHandledBy(Unknown156PacketHandler.class)
public record Unknown156Packet() implements InboundPacket {

    public static InboundPacket decode(ByteBuf buffer) {
        return new Unknown156Packet();
    }
}
