package work.fking.pangya.game.packet.handler;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.game.net.ClientGamePacketHandler;
import work.fking.pangya.game.GameServer;
import work.fking.pangya.game.player.Player;
import work.fking.pangya.networking.protocol.ProtocolUtils;

public class UpdateChatMacrosPacketHandler implements ClientGamePacketHandler {

    private static final int MAX_MACROS = 9;
    private static final int MACRO_LENGTH = 64;

    @Override
    public void handle(GameServer server, Player player, ByteBuf packet) {
        var macros = new String[MAX_MACROS];

        for (int i = 0; i < MAX_MACROS; i++) {
            macros[i] = ProtocolUtils.readFixedSizeString(packet, MACRO_LENGTH);
        }
        System.out.println(macros);
    }
}
