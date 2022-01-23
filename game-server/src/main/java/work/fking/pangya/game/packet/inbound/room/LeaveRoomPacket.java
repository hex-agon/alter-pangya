package work.fking.pangya.game.packet.inbound.room;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.game.packet.handler.room.LeaveRoomPacketHandler;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.Packet;
import work.fking.pangya.networking.protocol.PacketFactory;

@Packet(id = 0xf, handledBy = LeaveRoomPacketHandler.class)
public record LeaveRoomPacket(int roomId) implements InboundPacket {

    @PacketFactory
    public static InboundPacket decode(ByteBuf buffer) {
        buffer.readByte(); // unknown (either 0 or 1)
        var roomId = buffer.readShortLE();
        buffer.readIntLE(); // unknown
        buffer.readIntLE(); // unknown
        buffer.readIntLE(); // unknown
        buffer.readIntLE(); // unknown
        return new LeaveRoomPacket(roomId);
    }
}
