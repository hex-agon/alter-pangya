package work.fking.pangya.game.net

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import org.slf4j.LoggerFactory
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.player.Player

private val LOGGER = LoggerFactory.getLogger(ClientPacketDispatcher::class.java)

class ClientPacketDispatcher(
    private val gameServer: GameServer,
    private val player: Player,
    private val handlers: Array<ClientPacketHandler?>
) : SimpleChannelInboundHandler<ClientPacket>() {

    override fun channelRead0(ctx: ChannelHandlerContext, packet: ClientPacket) {
        val packetId = packet.type.id()
        val handler = handlers[packetId] ?: throw IllegalStateException("Packet " + packet.type + " has no attached handler")
        val buffer = packet.buffer
        try {
            handler.handle(gameServer, player, buffer)
        } finally {
            buffer.release()
        }
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        LOGGER.error("Exception caught in dispatcher", cause)
        ctx.disconnect()
    }
}
