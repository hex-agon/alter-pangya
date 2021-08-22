package work.fking.pangya.game.packet.inbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.protocol.InboundPacket;

public record RareShopOpenPacket() implements InboundPacket {

    public static InboundPacket decode(ByteBuf buffer) {
        return new RareShopOpenPacket();
    }
}
