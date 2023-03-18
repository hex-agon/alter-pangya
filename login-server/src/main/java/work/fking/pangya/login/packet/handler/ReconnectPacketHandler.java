package work.fking.pangya.login.packet.handler;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.login.LoginServer;
import work.fking.pangya.login.Player;
import work.fking.pangya.login.net.ClientLoginPacketHandler;
import work.fking.pangya.login.packet.outbound.LoginReplies;
import work.fking.pangya.login.packet.outbound.LoginReplies.Error;
import work.fking.pangya.networking.protocol.ProtocolUtils;

public class ReconnectPacketHandler implements ClientLoginPacketHandler {

    @Override
    public void handle(LoginServer server, Player player, ByteBuf packet) {
        String username = ProtocolUtils.readPString(packet);
        int userId = packet.readIntLE();
        String loginKey = ProtocolUtils.readPString(packet);

        player.channel().writeAndFlush(LoginReplies.error(Error.INVALID_ID_PW));
    }
}
