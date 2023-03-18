package work.fking.pangya.game.packet.handler;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.game.net.ClientGamePacketHandler;
import work.fking.pangya.game.GameServer;
import work.fking.pangya.game.Player;
import work.fking.pangya.game.model.PangCharacter;
import work.fking.pangya.networking.protocol.ProtocolException;

import java.util.Arrays;

public class EquipmentUpdatePacketHandler implements ClientGamePacketHandler {

    private static final int TYPE_UNKNOWN0 = 0;
    private static final int TYPE_CONSUMABLES = 2;
    private static final int TYPE_UNKNOWN5 = 5;
    private static final int TYPE_UNKNOWN9 = 9;

    @Override
    public void handle(GameServer server, Player player, ByteBuf packet) {
        var type = packet.readByte();

        switch (type) {
            case TYPE_UNKNOWN0 -> decodeUnknown0(packet);
            case TYPE_CONSUMABLES -> decodeConsumables(packet);
            case TYPE_UNKNOWN9 -> decodeUnknown9(packet);
            default -> System.out.println("Unhandled type " + type);
        }
    }

    private static void decodeUnknown0(ByteBuf buffer) {
        var character = PangCharacter.decode(buffer);
        System.out.println(character);
        System.out.println(Arrays.toString(character.equipmentIffIds()));
        System.out.println(Arrays.toString(character.equipmentUniqueIds()));
        System.out.println(Arrays.toString(character.cardIffIds()));
    }

    private static void decodeConsumables(ByteBuf buffer) {
        for (int i = 0; i < 10; i++) {
            buffer.readIntLE();
        }
    }

    private static void decodeUnknown9(ByteBuf buffer) {
        buffer.skipBytes(20);
    }
}
