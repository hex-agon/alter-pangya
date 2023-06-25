package work.fking.pangya.game.packet.outbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.protocol.OutboundPacket;

public class SelectChannelResultPacket implements OutboundPacket {

    @Override
    public void encode(ByteBuf buffer) {
        buffer.writeShortLE(0x4e);
        buffer.writeByte(1); // 1 = ok, 2 = channel is full, 3 = cannot find channel, 4 = failed to retrieve channel info
    }
}
