package work.fking.pangya.login.packet.inbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.login.packet.handler.SelectCharacterPacketHandler;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.PacketHandledBy;
import work.fking.pangya.networking.protocol.PacketId;

@PacketId(0x8)
@PacketHandledBy(SelectCharacterPacketHandler.class)
public record SelectCharacterPacket(int characterId, int hairColor) implements InboundPacket {

    public static InboundPacket decode(ByteBuf buffer) {
        int characterId = buffer.readIntLE();
        int hairColor = buffer.readShortLE();

        return new SelectCharacterPacket(characterId, hairColor);
    }
}
