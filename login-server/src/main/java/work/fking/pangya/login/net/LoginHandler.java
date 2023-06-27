package work.fking.pangya.login.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import work.fking.pangya.login.LoginServer;
import work.fking.pangya.login.task.LoginTask;
import work.fking.pangya.networking.crypt.PangCrypt;

import static work.fking.pangya.networking.protocol.ProtocolUtils.readPString;
import static work.fking.pangya.networking.protocol.ProtocolUtils.readPStringCharArray;

public class LoginHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginHandler.class);
    private static final int PACKET_ID = 0x1;

    private final LoginServer loginServer;
    private final int cryptKey;

    private LoginHandler(LoginServer loginServer, int cryptKey) {
        this.loginServer = loginServer;
        this.cryptKey = cryptKey;
    }

    public static LoginHandler create(LoginServer loginServer, int cryptKey) {
        return new LoginHandler(loginServer, cryptKey);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buffer) {
        PangCrypt.decrypt(buffer, cryptKey);
        int packetId = buffer.readShortLE();

        if (packetId != PACKET_ID) {
            LOGGER.warn("Unexpected packet during handover, packetId={}", packetId);
            ctx.disconnect();
            return;
        }
        var username = readPString(buffer);
        var passwordMd5 = readPStringCharArray(buffer);

        var channel = ctx.channel();
        LOGGER.debug("Queueing login request from {} for {}", channel.remoteAddress(), username);
        loginServer.submitTask(new LoginTask(loginServer, channel, cryptKey, username, passwordMd5));
    }
}
