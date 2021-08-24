package work.fking.pangya.game.packet.inbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.game.packet.handler.UpdateChatMacrosPacketHandler;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.PacketHandledBy;
import work.fking.pangya.networking.protocol.PacketId;
import work.fking.pangya.networking.protocol.ProtocolUtils;

import java.util.ArrayList;
import java.util.List;

@PacketId(0x69)
@PacketHandledBy(UpdateChatMacrosPacketHandler.class)
public record UpdateChatMacrosPacket(List<String> macros) implements InboundPacket {

    private static final int MAX_MACROS = 9;
    private static final int MACRO_LENGTH = 64;

    public static InboundPacket decode(ByteBuf buffer) {
        List<String> macros = new ArrayList<>(MAX_MACROS);

        for (int i = 0; i < MAX_MACROS; i++) {
            macros.add(ProtocolUtils.readFixedSizeString(buffer, MACRO_LENGTH));
        }
        return new UpdateChatMacrosPacket(macros);
    }
}
