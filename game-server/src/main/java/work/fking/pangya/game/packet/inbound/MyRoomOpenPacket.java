package work.fking.pangya.game.packet.inbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.game.packet.handler.MyRoomOpenPacketHandler;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.Packet;

@Packet(id = 0xb5, handledBy = MyRoomOpenPacketHandler.class)
public record MyRoomOpenPacket() implements InboundPacket {

    public static InboundPacket decode(ByteBuf buffer) {
        var selfUserId = buffer.readIntLE();
        var targetUserId = buffer.readIntLE();

        return new MyRoomOpenPacket();
    }
}
