package work.fking.pangya.game.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import work.fking.pangya.game.GameServer;
import work.fking.pangya.game.player.Player;

public class ClientPacketDispatcher extends SimpleChannelInboundHandler<ClientPacket> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientPacketDispatcher.class);

    private final GameServer gameServer;
    private final Player player;
    private final ClientPacketHandler[] handlers;

    private ClientPacketDispatcher(GameServer gameServer, Player player, ClientPacketHandler[] handlers) {
        this.gameServer = gameServer;
        this.player = player;
        this.handlers = handlers;
    }

    public static ClientPacketDispatcher create(GameServer gameServer, Player player, ClientPacketHandler[] handlers) {
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
            handler.handle(gameServer, player, buffer);
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
