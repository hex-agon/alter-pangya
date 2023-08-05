package work.fking.pangya.login.net.pipe

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.unix.Errors.NativeIoException
import io.netty.handler.codec.TooLongFrameException
import io.netty.handler.timeout.ReadTimeoutException
import org.slf4j.LoggerFactory
import work.fking.pangya.login.LoginServer
import work.fking.pangya.login.packet.outbound.LoginReplies
import work.fking.pangya.login.task.LoginTask
import work.fking.pangya.networking.crypt.PangCrypt
import work.fking.pangya.networking.crypt.PangCrypt.PangCryptException
import work.fking.pangya.networking.protocol.readPString
import work.fking.pangya.networking.protocol.readPStringCharArray

private const val LOGIN_PACKET_ID = 0x1
private const val RECONNECT_PACKET_ID = 0xb

private val LOGGER = LoggerFactory.getLogger(LoginHandler::class.java)

class LoginHandler(
    private val loginServer: LoginServer,
    private val cryptKey: Int
) : SimpleChannelInboundHandler<ByteBuf>() {

    override fun channelRead0(ctx: ChannelHandlerContext, buffer: ByteBuf) {
        PangCrypt.decrypt(buffer, cryptKey)

        if (buffer.readableBytes() < 2) return

        when (val packetId = buffer.readShortLE().toInt()) {
            LOGIN_PACKET_ID -> handleLogin(ctx, buffer)
            RECONNECT_PACKET_ID -> handleReconnect(ctx, buffer)
            else -> {
                LOGGER.warn("Unexpected packet during login, packetId={}", packetId)
                ctx.disconnect()
            }
        }
    }

    private fun handleLogin(ctx: ChannelHandlerContext, buffer: ByteBuf) {
        val username = buffer.readPString()
        val passwordMd5 = buffer.readPStringCharArray()
        val channel = ctx.channel()
        LOGGER.debug("Queueing login request from {} for {}", channel.remoteAddress(), username)
        loginServer.submitTask(LoginTask(loginServer, channel, cryptKey, username, passwordMd5))
    }

    private fun handleReconnect(ctx: ChannelHandlerContext, buffer: ByteBuf) {
        val username = buffer.readPString()
        val playerUid = buffer.readIntLE()
        val loginKey = buffer.readPString()

        LOGGER.debug("Rejecting reconnect for username={} uid={} loginKey={}", username, playerUid, loginKey)
        ctx.writeAndFlush(LoginReplies.error(LoginReplies.Error.INCORRECT_USERNAME_PASSWORD))
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        when (cause) {
            is NativeIoException -> ctx.disconnect()
            is TooLongFrameException -> ctx.disconnect()
            is PangCryptException -> ctx.disconnect()
            is IndexOutOfBoundsException -> {
                LOGGER.warn("{} - {}", ctx.channel().remoteAddress(), cause.message)
                ctx.disconnect()
            }

            is ReadTimeoutException -> {
                LOGGER.debug("{} read timeout", ctx.channel().remoteAddress())
                ctx.disconnect()
            }

            else -> {
                LOGGER.warn("Exception caught in the pipeline for client ${ctx.channel().remoteAddress()}", cause)
                ctx.disconnect()
            }
        }
    }
}