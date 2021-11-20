package work.fking.pangya.game.packet.inbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.game.packet.handler.RareShopOpenPacketHandler;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.Packet;

@Packet(id = 0x98, handledBy = RareShopOpenPacketHandler.class)
public record RareShopOpenPacket() implements InboundPacket {

    public static InboundPacket decode(ByteBuf buffer) {
        return new RareShopOpenPacket();
    }
}
