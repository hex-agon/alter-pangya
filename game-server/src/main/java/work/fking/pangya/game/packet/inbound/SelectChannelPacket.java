package work.fking.pangya.game.packet.inbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.game.packet.handler.SelectChannelPacketHandler;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.Packet;

@Packet(id = 0x4, handledBy = SelectChannelPacketHandler.class)
public record SelectChannelPacket(int channelId) implements InboundPacket {

    public static InboundPacket decode(ByteBuf buffer) {
        return new SelectChannelPacket(buffer.readUnsignedByte());
    }
}
