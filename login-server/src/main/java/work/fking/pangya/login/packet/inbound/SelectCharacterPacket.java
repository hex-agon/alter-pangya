package work.fking.pangya.login.packet.inbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.PacketFactory;

public record SelectCharacterPacket(int characterId, int hairColor) implements InboundPacket {

    @PacketFactory
    public static InboundPacket decode(ByteBuf buffer) {
        int characterId = buffer.readIntLE();
        int hairColor = buffer.readShortLE();

        return new SelectCharacterPacket(characterId, hairColor);
    }
}
