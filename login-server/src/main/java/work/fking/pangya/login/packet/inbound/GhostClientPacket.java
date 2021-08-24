package work.fking.pangya.login.packet.inbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.login.packet.handler.GhostClientPacketHandler;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.PacketHandledBy;
import work.fking.pangya.networking.protocol.PacketId;

@PacketId(0x4)
@PacketHandledBy(GhostClientPacketHandler.class)
public record GhostClientPacket() implements InboundPacket {

    public static InboundPacket decode(ByteBuf buffer) {
        return new GhostClientPacket();
    }
}
