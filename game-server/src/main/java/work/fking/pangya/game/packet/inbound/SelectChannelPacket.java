package work.fking.pangya.game.packet.inbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.game.packet.handler.SelectChannelPacketHandler;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.PacketHandledBy;
import work.fking.pangya.networking.protocol.PacketId;

@PacketId(0x4)
@PacketHandledBy(SelectChannelPacketHandler.class)
public record SelectChannelPacket(int channelId) implements InboundPacket {

    public static InboundPacket decode(ByteBuf buffer) {
        return new SelectChannelPacket(buffer.readUnsignedByte());
    }
}
