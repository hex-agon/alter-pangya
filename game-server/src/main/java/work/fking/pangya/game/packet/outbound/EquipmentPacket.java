package work.fking.pangya.game.packet.outbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.game.player.Player;
import work.fking.pangya.game.player.Equipment;
import work.fking.pangya.networking.protocol.OutboundPacket;

public class EquipmentPacket implements OutboundPacket {

    private static final int ID = 0x72;

    private final Equipment equipment;

    private EquipmentPacket(Equipment equipment) {
        this.equipment = equipment;
    }

    public static EquipmentPacket create(Player player) {
        return new EquipmentPacket(player.equipment());
    }

    @Override
    public void encode(ByteBuf buffer) {
        buffer.writeShortLE(ID);

        equipment.encode(buffer);
    }
}
