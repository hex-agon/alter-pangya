package work.fking.pangya.game.packet.inbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.game.packet.handler.RareShopOpenPacketHandler;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.PacketHandledBy;
import work.fking.pangya.networking.protocol.PacketId;

@PacketId(0x98)
@PacketHandledBy(RareShopOpenPacketHandler.class)
public record RareShopOpenPacket() implements InboundPacket {

    public static InboundPacket decode(ByteBuf buffer) {
        return new RareShopOpenPacket();
    }
}
