package work.fking.pangya.game.packet.outbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.protocol.OutboundPacket;

public class SelectChannelResultPacket implements OutboundPacket {

    @Override
    public void encode(ByteBuf target) {
        target.writeBytes(new byte[] {0x4E, 0x00, (byte) 0x01});
    }
}
