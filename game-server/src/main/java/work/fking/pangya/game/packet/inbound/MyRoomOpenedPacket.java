package work.fking.pangya.game.packet.inbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.game.packet.handler.MyRoomOpenedPacketHandler;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.Packet;

@Packet(id = 0xb7, handledBy = MyRoomOpenedPacketHandler.class)
public record MyRoomOpenedPacket() implements InboundPacket {

    public static InboundPacket decode(ByteBuf buffer) {
        var selfUserId = buffer.readIntLE();
        var unknown = buffer.readByte();

        return new MyRoomOpenedPacket();
    }
}
