package work.fking.pangya.networking.protocol;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.jodah.typetools.TypeResolver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

@Sharable
public class InboundPacketDispatcher extends SimpleChannelInboundHandler<InboundPacket> {

    private static final Logger LOGGER = LogManager.getLogger(InboundPacketDispatcher.class);

    private final Map<Class<? extends InboundPacket>, InboundPacketHandler<? extends InboundPacket>> handlers;

    private InboundPacketDispatcher(Map<Class<? extends InboundPacket>, InboundPacketHandler<? extends InboundPacket>> handlers) {
        this.handlers = handlers;
    }

    public static Builder builder(InboundPacketHandlerFactory handlerFactory) {
        return new Builder(handlerFactory);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void channelRead0(ChannelHandlerContext ctx, InboundPacket packet) {
        // this cast is safe, checked by the builder.handler method
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

    public static class Builder {

        private final Map<Class<? extends InboundPacket>, InboundPacketHandler<? extends InboundPacket>> handlers = new HashMap<>();
        private final InboundPacketHandlerFactory handlerFactory;

        public Builder(InboundPacketHandlerFactory handlerFactory) {
            this.handlerFactory = handlerFactory;
        }

        public Builder register(Class<? extends InboundPacket> inboundPacket) {
            var packet = inboundPacket.getAnnotation(Packet.class);

            if (packet == null) {
                LOGGER.warn("InboundPacket class {} is missing the @Packet annotation, skipping registration", inboundPacket.getSimpleName());
                return this;
            }
            var handlerClass = packet.handledBy();
            var actualPacketType = TypeResolver.resolveRawArgument(InboundPacketHandler.class, handlerClass);

            if (!inboundPacket.equals(actualPacketType)) {
                LOGGER.warn("Unexpected packet type in handler {}, expected {}, got {}", handlerClass.getSimpleName(), inboundPacket.getSimpleName(), actualPacketType.getSimpleName());
                return this;
            }
            handlers.put(inboundPacket, handlerFactory.create(handlerClass));
            return this;
        }

        public InboundPacketDispatcher build() {
            return new InboundPacketDispatcher(handlers);
        }
    }

    public interface InboundPacketHandlerFactory {

        InboundPacketHandler<? extends InboundPacket> create(Class<? extends InboundPacketHandler<? extends InboundPacket>> packetHandlerClass);
    }
}
