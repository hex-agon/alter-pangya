package work.fking.pangya.networking.protocol;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@Sharable
public class InboundPacketDispatcher extends SimpleChannelInboundHandler<InboundPacket> {

    private final Map<Class<? extends InboundPacket>, InboundPacketHandler<? extends InboundPacket>> handlers = new HashMap<>();
    private final InboundPacketHandlerFactory handlerFactory;

    private InboundPacketDispatcher(InboundPacketHandlerFactory handlerFactory) {
        this.handlerFactory = handlerFactory;
    }

    public static InboundPacketDispatcher create(InboundPacketHandlerFactory handlerFactory) {
        return new InboundPacketDispatcher(handlerFactory);
    }

    public <P extends InboundPacket> InboundPacketDispatcher registerHandler(Class<P> inboundPacketClass, Class<? extends InboundPacketHandler<P>> packetHandlerClass) {
        handlers.put(inboundPacketClass, handlerFactory.create(packetHandlerClass));
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void channelRead0(ChannelHandlerContext ctx, InboundPacket packet) {
        // this cast is safe, checked by registerHandler method
        InboundPacketHandler<InboundPacket> packetHandler = (InboundPacketHandler<InboundPacket>) handlers.get(packet.getClass());

        if (packetHandler == null) {
            LOGGER.warn("Inbound packet type={} does not have an associated handler!", packet.getClass());
            return;
        }
        packetHandler.handle(ctx.channel(), packet);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("Exception caught on packet dispatcher", cause);
    }
}
