package work.fking.pangya.resources;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Sharable
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final Logger LOGGER = LogManager.getLogger(HttpServerHandler.class);

    private static final ByteBuf INTERNAL_ERROR_BUFFER = Unpooled.copiedBuffer("Internal server error", CharsetUtil.UTF_8);
    private static final String URI_TRANSLATIONS_REQUEST = "/Translation/Read.aspx";
    private static final String URI_UPDATELIST_REQUEST = "/new/Service/S4_Patch/updatelist";

    private final ResourceLoader resourceLoader = new ResourceLoader();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) {

        if (request.method() != HttpMethod.GET) {
            reply(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED, INTERNAL_ERROR_BUFFER);
            return;
        }
        String uri = request.uri();
        LOGGER.trace("Incoming request uri={}", uri);

        switch (uri) {
            case URI_TRANSLATIONS_REQUEST -> serveResource(ctx, "/translation.txt");
            case URI_UPDATELIST_REQUEST -> serveResource(ctx, "/updatelist.txt");
            default -> reply(ctx, HttpResponseStatus.NOT_FOUND, Unpooled.EMPTY_BUFFER);
        }
    }

    private void serveResource(ChannelHandlerContext ctx, String resourceName) {
        ByteBuf byteBuf = resourceLoader.load(resourceName);

        if (byteBuf != null) {
            reply(ctx, HttpResponseStatus.OK, byteBuf);
        } else {
            reply(ctx, HttpResponseStatus.NOT_FOUND, Unpooled.EMPTY_BUFFER);
        }
    }

    private void reply(ChannelHandlerContext ctx, HttpResponseStatus status, ByteBuf content) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, content);

        HttpHeaders headers = response.headers();
        headers.set(HttpHeaderNames.CONTENT_TYPE, "text/html");
        headers.set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

        ctx.writeAndFlush(response)
           .addListener(ChannelFutureListener.CLOSE);
    }
}
