package work.fking.pangya.login.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import work.fking.pangya.login.LoginServer;
import work.fking.pangya.login.Player;

public class ClientPacketDispatcher extends SimpleChannelInboundHandler<ClientPacket> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientPacketDispatcher.class);

    private final LoginServer loginServer;
    private final Player player;
    private final ClientPacketHandler[] handlers;

    private ClientPacketDispatcher(LoginServer loginServer, Player player, ClientPacketHandler[] handlers) {
        this.loginServer = loginServer;
        this.player = player;
        this.handlers = handlers;
    }

    public static ClientPacketDispatcher create(LoginServer gameServer, Player player, ClientPacketHandler[] handlers) {
        return new ClientPacketDispatcher(gameServer, player, handlers);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ClientPacket packet) {
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
