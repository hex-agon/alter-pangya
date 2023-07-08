package work.fking.pangya.login.net

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import org.slf4j.LoggerFactory
import work.fking.pangya.login.LoginServer
import work.fking.pangya.login.task.LoginTask
import work.fking.pangya.networking.crypt.PangCrypt
import work.fking.pangya.networking.protocol.readPString
import work.fking.pangya.networking.protocol.readPStringCharArray

private const val PACKET_ID = 0x1

private val LOGGER = LoggerFactory.getLogger(LoginHandler::class.java)

class LoginHandler(
    private val loginServer: LoginServer,
    private val cryptKey: Int
) : SimpleChannelInboundHandler<ByteBuf>() {

    override fun channelRead0(ctx: ChannelHandlerContext, buffer: ByteBuf) {
        PangCrypt.decrypt(buffer, cryptKey)
        val packetId = buffer.readShortLE().toInt()
        if (packetId != PACKET_ID) {
            LOGGER.warn("Unexpected packet during handover, packetId={}", packetId)
            ctx.disconnect()
            return
        }
        val username = buffer.readPString()
        val passwordMd5 = buffer.readPStringCharArray()
        val channel = ctx.channel()
        LOGGER.debug("Queueing login request from {} for {}", channel.remoteAddress(), username)
        loginServer.submitTask(LoginTask(loginServer, channel, cryptKey, username, passwordMd5))
    }
}
