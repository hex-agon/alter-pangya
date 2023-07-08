package work.fking.pangya.resources

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.DefaultFullHttpResponse
import io.netty.handler.codec.http.FullHttpRequest
import io.netty.handler.codec.http.FullHttpResponse
import io.netty.handler.codec.http.HttpHeaderNames
import io.netty.handler.codec.http.HttpMethod
import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.handler.codec.http.HttpVersion
import io.netty.util.CharsetUtil
import org.apache.logging.log4j.LogManager

private val LOGGER = LogManager.getLogger(HttpServerHandler::class.java)
private val INTERNAL_ERROR_BUFFER = Unpooled.copiedBuffer("Internal server error", CharsetUtil.UTF_8)
private const val URI_TRANSLATIONS_REQUEST = "/Translation/Read.aspx"
private const val URI_UPDATELIST_REQUEST = "/new/Service/S4_Patch/updatelist"

@Sharable
class HttpServerHandler : SimpleChannelInboundHandler<FullHttpRequest>() {

    private val resourceLoader = ResourceLoader()

    override fun channelRead0(ctx: ChannelHandlerContext, request: FullHttpRequest) {
        if (request.method() !== HttpMethod.GET) {
            reply(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED, INTERNAL_ERROR_BUFFER)
            return
        }
        val uri = request.uri()
        LOGGER.trace("Incoming request uri={}", uri)
        when (uri) {
            URI_TRANSLATIONS_REQUEST -> serveResource(ctx, "/translation.txt")
            URI_UPDATELIST_REQUEST -> serveResource(ctx, "/updatelist.txt")
            else -> reply(ctx, HttpResponseStatus.NOT_FOUND, Unpooled.EMPTY_BUFFER)
        }
    }

    private fun serveResource(ctx: ChannelHandlerContext, resourceName: String) {
        val byteBuf = resourceLoader.load(resourceName)
        if (byteBuf != null) {
            reply(ctx, HttpResponseStatus.OK, byteBuf)
        } else {
            reply(ctx, HttpResponseStatus.NOT_FOUND, Unpooled.EMPTY_BUFFER)
        }
    }

    private fun reply(ctx: ChannelHandlerContext, status: HttpResponseStatus, content: ByteBuf) {
        val response: FullHttpResponse = DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, content)
        val headers = response.headers()
        headers[HttpHeaderNames.CONTENT_TYPE] = "text/html"
        headers[HttpHeaderNames.CONTENT_LENGTH] = content.readableBytes()
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE)
    }

}
