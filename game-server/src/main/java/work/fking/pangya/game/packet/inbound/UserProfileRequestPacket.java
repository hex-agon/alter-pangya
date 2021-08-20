package work.fking.pangya.game.packet.inbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.PacketFactory;

public record UserProfileRequestPacket() implements InboundPacket {

    @PacketFactory
    public static InboundPacket decode(ByteBuf buffer) {
        var userId = buffer.readIntLE();
        var type = buffer.readByte();
        return new UserProfileRequestPacket();
    }
}
