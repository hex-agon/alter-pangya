package work.fking.pangya.login;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.fking.pangya.login.model.LoginSession;

/**
 * A class that simply validates if the channel has a {@link LoginSession} attached to it.
 */
@Sharable
public class LoginSessionChecker extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LogManager.getLogger(LoginSessionChecker.class);

    public static final LoginSessionChecker INSTANCE = new LoginSessionChecker();

    private LoginSessionChecker() {
    }

    public static LoginSessionChecker instance() {
        return INSTANCE;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (!ctx.channel().hasAttr(LoginSession.KEY)) {
            LOGGER.warn("Connection from {} has no LoginSession attached!", ctx.channel().remoteAddress());
            ctx.disconnect();
        } else {
            super.channelRead(ctx, msg);
        }
    }
}
