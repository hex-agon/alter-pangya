package work.fking.pangya.login.packet.handler;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.login.LoginServer;
import work.fking.pangya.login.Player;
import work.fking.pangya.login.net.ClientLoginPacketHandler;

public class GhostClientPacketHandler implements ClientLoginPacketHandler {

    @Override
    public void handle(LoginServer server, Player player, ByteBuf packet) {
    }
}
