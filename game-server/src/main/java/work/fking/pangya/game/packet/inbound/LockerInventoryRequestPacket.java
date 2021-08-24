package work.fking.pangya.game.packet.inbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.game.packet.handler.LockerInventoryRequestPacketHandler;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.PacketHandledBy;
import work.fking.pangya.networking.protocol.PacketId;

@PacketId(0xd3)
@PacketHandledBy(LockerInventoryRequestPacketHandler.class)
public class LockerInventoryRequestPacket implements InboundPacket {

    public static InboundPacket decode(ByteBuf buffer) {
        return new LockerInventoryRequestPacket();
    }
}
