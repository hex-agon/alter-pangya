package work.fking.pangya.login.packet.inbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.login.packet.handler.SetNicknamePacketHandler;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.PacketHandledBy;
import work.fking.pangya.networking.protocol.PacketId;
import work.fking.pangya.networking.protocol.ProtocolUtils;

@PacketId(0x6)
@PacketHandledBy(SetNicknamePacketHandler.class)
public record SetNicknamePacket(String nickname) implements InboundPacket {

    public static InboundPacket decode(ByteBuf buffer) {
        String nickname = ProtocolUtils.readPString(buffer);

        return new SetNicknamePacket(nickname);
    }
}
