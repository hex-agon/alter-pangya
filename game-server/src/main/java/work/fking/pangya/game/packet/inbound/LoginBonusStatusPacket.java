package work.fking.pangya.game.packet.inbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.game.packet.handler.LoginBonusStatusPacketHandler;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.Packet;

@Packet(id = 0x16e, handledBy = LoginBonusStatusPacketHandler.class)
public record LoginBonusStatusPacket() implements InboundPacket {

    private static final LoginBonusStatusPacket INSTANCE = new LoginBonusStatusPacket();

    public static InboundPacket decode(ByteBuf buffer) {
        return INSTANCE;
    }
}
