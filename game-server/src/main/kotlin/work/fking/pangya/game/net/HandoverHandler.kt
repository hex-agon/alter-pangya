package work.fking.pangya.game.net

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.unix.Errors
import io.netty.handler.codec.TooLongFrameException
import io.netty.handler.timeout.ReadTimeoutException
import org.slf4j.LoggerFactory
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.task.HandoverTask
import work.fking.pangya.networking.crypt.PangCrypt
import work.fking.pangya.networking.protocol.readPString

private val LOGGER = LoggerFactory.getLogger(HandoverHandler::class.java)
private const val PACKET_ID = 0x2

class HandoverHandler(
    private val gameServer: GameServer,
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
        val uid = buffer.readIntLE()
        buffer.readIntLE()
        buffer.readShortLE()
        val loginKey = buffer.readPString()
        val clientVersion = buffer.readPString()
        buffer.readIntLE() // check c https://github.com/hsreina/pangya-server/blob/449140f97592d5d403ef0df01d19ca2c6c834361/src/Server/Sync/SyncServer.pas#L411
        buffer.readIntLE()
        val sessionKey = buffer.readPString()
        LOGGER.info("Successful handover username={}, uid={}, clientVersion={}, loginKey={}, sessionKey={}", username, uid, clientVersion, loginKey, sessionKey)
        gameServer.runTask(HandoverTask(gameServer, ctx.channel(), cryptKey, loginKey))
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        when (cause) {
            is Errors.NativeIoException -> ctx.disconnect()
            is TooLongFrameException -> ctx.disconnect()
            is PangCrypt.PangCryptException -> ctx.disconnect()
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
