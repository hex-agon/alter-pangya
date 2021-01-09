package work.fking.pangya.game.packet.inbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.PacketFactory;

public record LoginBonusStatusPacket() implements InboundPacket {

    private static final LoginBonusStatusPacket INSTANCE = new LoginBonusStatusPacket();

    @PacketFactory
    public static InboundPacket decode(ByteBuf buffer) {
        return INSTANCE;
    }
}
