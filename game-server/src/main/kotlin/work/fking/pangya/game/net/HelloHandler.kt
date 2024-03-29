package work.fking.pangya.game.net

import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import io.netty.handler.timeout.ReadTimeoutHandler
import org.apache.logging.log4j.LogManager
import work.fking.pangya.game.GameServer
import work.fking.pangya.game.Rand
import work.fking.pangya.game.packet.outbound.HelloPacket
import work.fking.pangya.networking.crypt.PangCrypt
import work.fking.pangya.networking.protocol.ProtocolEncoder
import java.nio.ByteOrder
import java.util.concurrent.TimeUnit

private val LOGGER = LogManager.getLogger(HelloHandler::class.java)

@Sharable
class HelloHandler(
    private val gameServer: GameServer
) : ChannelInboundHandlerAdapter() {

    override fun channelActive(ctx: ChannelHandlerContext) {
        val channel = ctx.channel()
        val cryptKey = Rand.maxInclusive(PangCrypt.CRYPT_KEY_MAX)
        LOGGER.debug("New connection from {}, selected cryptKey={}", channel.remoteAddress(), cryptKey)
        ctx.writeAndFlush(HelloPacket(cryptKey))

        val pipeline = ctx.pipeline()
        pipeline.remove(this)
        pipeline.replace("encoder", "protocolEncoder", ProtocolEncoder(cryptKey))
        pipeline.addLast("timeoutHandler", ReadTimeoutHandler(10, TimeUnit.SECONDS))
        pipeline.addLast("framer", LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN, 2500, 1, 2, 1, 0, true))
        pipeline.addLast("handoverHandler", HandoverHandler(gameServer, cryptKey))
    }
}
