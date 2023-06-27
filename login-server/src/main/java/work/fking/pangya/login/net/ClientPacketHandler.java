package work.fking.pangya.login.net;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.login.LoginServer;
import work.fking.pangya.login.Player;

public interface ClientPacketHandler {

    void handle(LoginServer server, Player player, ByteBuf packet);
}
