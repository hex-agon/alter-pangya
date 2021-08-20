package work.fking.pangya.game.packet.outbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.networking.protocol.OutboundPacket;
import work.fking.pangya.networking.protocol.ProtocolUtils;

public class PlayerDataPacket implements OutboundPacket {

    @Override
    public void encode(ByteBuf target) {

        // General info
        target.writeShortLE(0xFFFF);
        ProtocolUtils.writeFixedSizeString(target, "hexagon", 21); // username
        ProtocolUtils.writeFixedSizeString(target, "Hex agon", 21); // nickname
        target.writeZero(32);
        target.writeByte(0xF); // gm flag
        target.writeZero(6);
        target.writeIntLE(0); // connectionId
        target.writeZero(31);
        target.writeByte(0); // chat restricted? 0xf yes
        target.writeZero(138);
    }
}
