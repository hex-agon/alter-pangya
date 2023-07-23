package work.fking.pangya.login.net.pipe

import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import org.slf4j.LoggerFactory
import work.fking.pangya.login.LoginServer
import work.fking.pangya.login.Rand
import work.fking.pangya.login.packet.outbound.HelloPacket
import work.fking.pangya.networking.crypt.PangCrypt
import work.fking.pangya.networking.protocol.ProtocolEncoder
import java.nio.ByteOrder

private val LOGGER = LoggerFactory.getLogger(HelloHandler::class.java)

@ChannelHandler.Sharable
class HelloHandler(
    private val loginServer: LoginServer
) : ChannelInboundHandlerAdapter() {

    override fun channelActive(ctx: ChannelHandlerContext) {
        val channel = ctx.channel()
        val cryptKey = Rand.maxInclusive(PangCrypt.CRYPT_KEY_MAX)
        LOGGER.debug("New connection from {}, selected cryptKey={}", channel.remoteAddress(), cryptKey)
        ctx.writeAndFlush(HelloPacket(cryptKey))
        val pipeline = channel.pipeline()
        pipeline.remove(this)
        pipeline.replace("encoder", "encoder", ProtocolEncoder(cryptKey))
        pipeline.addLast("framer", LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN, 10000, 1, 2, 1, 0, true))
        pipeline.addLast("loginHandler", LoginHandler(loginServer, cryptKey))
    }
}