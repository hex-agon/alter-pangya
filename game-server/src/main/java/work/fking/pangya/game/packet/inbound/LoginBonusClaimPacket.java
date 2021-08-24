package work.fking.pangya.game.packet.inbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.game.packet.handler.LoginBonusClaimPacketHandler;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.PacketHandledBy;
import work.fking.pangya.networking.protocol.PacketId;

@PacketId(0x16f)
@PacketHandledBy(LoginBonusClaimPacketHandler.class)
public record LoginBonusClaimPacket() implements InboundPacket {

    private static final LoginBonusClaimPacket INSTANCE = new LoginBonusClaimPacket();

    public static InboundPacket decode(ByteBuf buffer) {
        return INSTANCE;
    }
}
