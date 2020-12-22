package work.fking.pangya.login;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.fking.pangya.login.model.LoginSession;

@Sharable
public class TailHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LogManager.getLogger(TailHandler.class);
    private final LoginServer server;

    public TailHandler(LoginServer server) {
        this.server = server;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        LoginSession loginSession = channel.attr(LoginSession.KEY).get();
        LOGGER.debug("Channel inactive, ip={}", channel.remoteAddress());
        server.unregister(loginSession);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.catching(cause);
    }
}
