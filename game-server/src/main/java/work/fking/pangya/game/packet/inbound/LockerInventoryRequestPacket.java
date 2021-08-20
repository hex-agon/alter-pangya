package work.fking.pangya.game.packet.inbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.protocol.InboundPacket;

public class LockerInventoryRequestPacket implements InboundPacket {

    public static InboundPacket decode(ByteBuf buffer) {
        return new LockerInventoryRequestPacket();
    }
}
