package work.fking.pangya.game.packet.inbound;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.PacketFactory;

public record Unknown156Packet() implements InboundPacket {

    @PacketFactory
    public static InboundPacket decode(ByteBuf buffer) {
        return new Unknown156Packet();
    }
}
