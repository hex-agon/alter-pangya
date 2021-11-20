package work.fking.pangya.game.packet.inbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.game.model.PangCharacter;
import work.fking.pangya.game.packet.handler.EquipmentUpdatePacketHandler;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.Packet;
import work.fking.pangya.networking.protocol.ProtocolException;

import java.util.Arrays;

@Packet(id = 0x20, handledBy = EquipmentUpdatePacketHandler.class)
public class EquipmentUpdatePacket implements InboundPacket {

    private static final int TYPE_UNKNOWN0 = 0;
    private static final int TYPE_CONSUMABLES = 2;
    private static final int TYPE_UNKNOWN5 = 5;
    private static final int TYPE_UNKNOWN9 = 9;

    public static InboundPacket decode(ByteBuf buffer) {
        var type = buffer.readByte();

        return switch (type) {
            case TYPE_UNKNOWN0 -> decodeUnknown0(buffer);
            case TYPE_CONSUMABLES -> decodeConsumables(buffer);
            case TYPE_UNKNOWN5 -> decodeUnknown5(buffer);
            case TYPE_UNKNOWN9 -> decodeUnknown9(buffer);
            default -> throw new ProtocolException("Unhandled type " + type);
        };
    }

    private static InboundPacket decodeUnknown0(ByteBuf buffer) {
        var character = PangCharacter.decode(buffer);
        System.out.println(character);
        System.out.println(Arrays.toString(character.equipmentIffIds()));
        System.out.println(Arrays.toString(character.equipmentUniqueIds()));
        System.out.println(Arrays.toString(character.cardIffIds()));
        return new EquipmentUpdatePacket();
    }

    private static InboundPacket decodeConsumables(ByteBuf buffer) {
        for (int i = 0; i < 10; i++) {
            buffer.readIntLE();
        }
        return new EquipmentUpdatePacket();
    }

    private static InboundPacket decodeUnknown5(ByteBuf buffer) {
        var unknown = buffer.readIntLE();
        var unknown2 = buffer.readIntLE();
        int[] parts = new int[24];
        int[] uniqueIds = new int[24];

        for (int i = 0; i < 24; i++) {
            parts[i] = buffer.readIntLE();
        }
        for (int i = 0; i < 24; i++) {
            uniqueIds[i] = buffer.readIntLE();
        }
        System.out.println(Arrays.toString(parts));
        System.out.println(Arrays.toString(uniqueIds));
        return new EquipmentUpdatePacket();
    }

    private static InboundPacket decodeUnknown9(ByteBuf buffer) {
        buffer.skipBytes(20);
        return new EquipmentUpdatePacket();
    }
}
