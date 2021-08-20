package work.fking.pangya.game.packet.inbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.protocol.InboundPacket;

public record MyRoomOpenPacket() implements InboundPacket {

    public static InboundPacket decode(ByteBuf buffer) {
        var selfUserId = buffer.readIntLE();
        var targetUserId = buffer.readIntLE();

        return new MyRoomOpenPacket();
    }
}
