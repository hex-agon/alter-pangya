package work.fking.pangya.game.packet.inbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.PacketId;

public record SelectChannelPacket(int channelId) implements InboundPacket {

    @PacketId
    public static InboundPacket decode(ByteBuf buffer) {
        return new SelectChannelPacket(buffer.readUnsignedByte());
    }
}
