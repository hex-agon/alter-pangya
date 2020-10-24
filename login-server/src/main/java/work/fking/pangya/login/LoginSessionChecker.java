package work.fking.pangya.login;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.log4j.Log4j2;
import work.fking.pangya.login.model.LoginSession;

/**
 * A class that simply validates if the channel has a {@link LoginSession} attached to it.
 */
@Log4j2
@Sharable
public class LoginSessionChecker extends ChannelInboundHandlerAdapter {

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
