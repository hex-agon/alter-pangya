package work.fking.pangya;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.log4j.Log4j2;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.packet.inbound.LoginRequestPacket;
import work.fking.pangya.packet.outbound.LoginResultPacket;
import work.fking.pangya.packet.outbound.LoginResultPacket.ErrorCode;

@Log4j2
public class PacketHandler extends SimpleChannelInboundHandler<InboundPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, InboundPacket msg) {
        System.out.println(msg.toString());

        if (msg instanceof LoginRequestPacket) {
            LoginResultPacket loginResultPacket = LoginResultPacket.builder()
                                                                   .error()
                                                                   .errorCode(ErrorCode.INVALID_CREDENTIALS)
                                                                   .build();
            ctx.channel().writeAndFlush(loginResultPacket);
        }
    }
}
