package work.fking.pangya.game.packet.outbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.protocol.OutboundPacket;
import work.fking.pangya.networking.protocol.ProtocolUtils;

public class ServerChannelsPacket implements OutboundPacket {

    private static final int ID = 77;

    @Override
    public void encode(ByteBuf target) {
        target.writeShortLE(ID);
        target.writeByte(1); // Server count

        ProtocolUtils.writeFixedSizeString(target, "ServerChannel", 64);
        target.writeBytes(new byte[47]);
        target.writeByte(1); // channelId
        target.writeIntLE(0);
        target.writeIntLE(0);
    }
}
