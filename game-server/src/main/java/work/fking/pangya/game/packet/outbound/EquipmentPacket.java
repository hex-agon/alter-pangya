package work.fking.pangya.game.packet.outbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.game.model.PangEquipment;
import work.fking.pangya.networking.protocol.OutboundPacket;

public class EquipmentPacket implements OutboundPacket {

    private static final int ID = 0x72;

    @Override
    public void encode(ByteBuf target) {
        target.writeShortLE(ID);

        PangEquipment.mock().encode(target);
    }
}
