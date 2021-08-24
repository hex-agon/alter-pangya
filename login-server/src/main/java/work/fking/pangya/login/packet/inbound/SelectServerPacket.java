package work.fking.pangya.login.packet.inbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.login.packet.handler.SelectServerPacketHandler;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.PacketHandledBy;
import work.fking.pangya.networking.protocol.PacketId;

@PacketId(0x3)
@PacketHandledBy(SelectServerPacketHandler.class)
public record SelectServerPacket(int serverId) implements InboundPacket {

    public static InboundPacket decode(ByteBuf buffer) {
        int serverId = buffer.readShortLE();
        buffer.skipBytes(2);

        return new SelectServerPacket(serverId);
    }
}
