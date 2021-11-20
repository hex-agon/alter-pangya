package work.fking.pangya.game.packet.inbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.game.packet.handler.LockerInventoryRequestPacketHandler;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.Packet;

@Packet(id = 0xd3, handledBy = LockerInventoryRequestPacketHandler.class)
public class LockerInventoryRequestPacket implements InboundPacket {

    public static InboundPacket decode(ByteBuf buffer) {
        return new LockerInventoryRequestPacket();
    }
}
