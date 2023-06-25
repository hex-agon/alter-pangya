package work.fking.pangya.game.packet.outbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.protocol.OutboundPacket;
import work.fking.pangya.networking.protocol.ProtocolUtils;

import static work.fking.pangya.networking.protocol.ProtocolUtils.writeFixedSizeString;

public class ServerChannelsPacket implements OutboundPacket {

    private static final int ID = 0x4d;

    @Override
    public void encode(ByteBuf target) {
        target.writeShortLE(ID);
        target.writeByte(1); // Server count

        writeFixedSizeString(target, "ServerChannel", 64);
        target.writeShortLE(20);
        target.writeShortLE(19);
        target.writeByte(1); // channelId
        target.writeIntLE(0);
    }
}
