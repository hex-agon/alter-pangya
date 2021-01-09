package work.fking.pangya.game.packet.inbound;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.PacketFactory;

public record Unknown320Packet() implements InboundPacket {

    @PacketFactory
    public static InboundPacket decode(ByteBuf buffer) {
        // sent when the shop is open
        System.out.println(ByteBufUtil.prettyHexDump(buffer));
        return new Unknown320Packet();
    }
}
