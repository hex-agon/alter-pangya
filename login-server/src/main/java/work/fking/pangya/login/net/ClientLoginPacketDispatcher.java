package work.fking.pangya.login.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import work.fking.pangya.login.LoginServer;
import work.fking.pangya.login.Player;

public class ClientLoginPacketDispatcher extends SimpleChannelInboundHandler<ClientLoginPacket> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientLoginPacketDispatcher.class);

    private final LoginServer loginServer;
    private final Player player;
    private final ClientLoginPacketHandler[] handlers;

    private ClientLoginPacketDispatcher(LoginServer loginServer, Player player, ClientLoginPacketHandler[] handlers) {
        this.loginServer = loginServer;
        this.player = player;
        this.handlers = handlers;
    }

    public static ClientLoginPacketDispatcher create(LoginServer gameServer, Player player, ClientLoginPacketHandler[] handlers) {
        return new ClientLoginPacketDispatcher(gameServer, player, handlers);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ClientLoginPacket packet) {
        var packetId = packet.type().id();
        var handler = handlers[packetId];

        if (handler == null) {
            throw new IllegalStateException("Packet " + packet.type() + " has no attached handler");
        }
        var buffer = packet.buffer();

        try {
            handler.handle(loginServer, player, buffer);
        } finally {
            buffer.release();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("Exception caught in dispatcher", cause);
        ctx.disconnect();
    }
}
