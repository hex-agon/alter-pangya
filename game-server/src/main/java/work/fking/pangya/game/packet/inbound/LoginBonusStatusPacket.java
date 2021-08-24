package work.fking.pangya.game.packet.inbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.PacketId;

public record LoginBonusStatusPacket() implements InboundPacket {

    private static final LoginBonusStatusPacket INSTANCE = new LoginBonusStatusPacket();

    @PacketId
    public static InboundPacket decode(ByteBuf buffer) {
        return INSTANCE;
    }
}
