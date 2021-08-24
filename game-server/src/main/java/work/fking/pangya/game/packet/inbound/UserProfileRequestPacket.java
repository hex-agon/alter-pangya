package work.fking.pangya.game.packet.inbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.game.packet.handler.UserProfileRequestPacketHandler;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.PacketHandledBy;
import work.fking.pangya.networking.protocol.PacketId;

@PacketId(0x2f)
@PacketHandledBy(UserProfileRequestPacketHandler.class)
public record UserProfileRequestPacket() implements InboundPacket {

    public static InboundPacket decode(ByteBuf buffer) {
        var userId = buffer.readIntLE();
        var type = buffer.readByte();
        return new UserProfileRequestPacket();
    }
}
