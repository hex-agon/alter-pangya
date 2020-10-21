package work.fking.pangya;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.log4j.Log4j2;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.packet.inbound.LoginRequestPacket;
import work.fking.pangya.packet.outbound.LoginResultPacket;
import work.fking.pangya.packet.outbound.LoginResultPacket.Result;

@Log4j2
public class PacketHandler extends SimpleChannelInboundHandler<InboundPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, InboundPacket packet) {

        if (packet instanceof LoginRequestPacket loginRequest) {
            LOGGER.debug("{}", loginRequest);
            LoginResultPacket loginResultPacket = LoginResultPacket.error(Result.GEO_BLOCKED)
                                                                   .notice("Something's fishy")
                                                                   .build();

            Channel channel = ctx.channel();
            channel.write(loginResultPacket);
            channel.flush();
        } else {
            LOGGER.warn("Unhandled inbound packet={}", packet);
        }
    }
}
