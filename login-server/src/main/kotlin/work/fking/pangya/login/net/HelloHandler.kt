package work.fking.pangya.login.net

import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import org.apache.logging.log4j.LogManager
import work.fking.pangya.common.Rand
import work.fking.pangya.login.LoginServer
import work.fking.pangya.login.packet.outbound.HelloPacket
import work.fking.pangya.networking.crypt.PangCrypt
import work.fking.pangya.networking.protocol.ProtocolEncoder
import java.nio.ByteOrder

@Sharable
class HelloHandler(
    private val loginServer: LoginServer
) : ChannelInboundHandlerAdapter() {

    companion object {
        @JvmStatic
        private val LOGGER = LogManager.getLogger(HelloHandler::class.java)
    }

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