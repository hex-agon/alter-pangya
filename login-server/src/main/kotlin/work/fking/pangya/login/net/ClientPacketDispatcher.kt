package work.fking.pangya.login.net

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.unix.Errors.NativeIoException
import org.slf4j.LoggerFactory
import work.fking.pangya.login.LoginServer
import work.fking.pangya.login.Player

private val LOGGER = LoggerFactory.getLogger(ClientPacketDispatcher::class.java)

class ClientPacketDispatcher(
    private val loginServer: LoginServer,
    private val player: Player,
    private val handlers: Array<ClientPacketHandler?>
) : SimpleChannelInboundHandler<ClientPacket>() {

    override fun channelRead0(ctx: ChannelHandlerContext, packet: ClientPacket) {
        val packetId = packet.type.id
        val handler = handlers[packetId] ?: throw IllegalStateException("Packet " + packet.type + " has no attached handler")
        val buffer = packet.buffer
        try {
            handler.handle(loginServer, player, buffer)
        } finally {
            buffer.release()
        }
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        when (cause) {
            is NativeIoException -> ctx.disconnect()
            else -> {
                LOGGER.error("Exception caught in dispatcher", cause)
                ctx.disconnect()
            }
        }
    }
}