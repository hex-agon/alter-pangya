package work.fking.pangya.networking.protocol;

import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.util.Arrays;

/**
 * A simple class representing a packet based protocol. This class also provides a factory method for the registered packets.
 */
public final class Protocol {

    private static final Logger LOGGER = LoggerFactory.getLogger(Protocol.class);

    private static final String FACTORY_METHOD_NAME = "decode";
    private static final MethodType FACTORY_METHOD_TYPE = MethodType.methodType(InboundPacket.class, ByteBuf.class);
    private static final Lookup LOOKUP = MethodHandles.lookup();

    private final MethodHandle[] inboundPacketConstructors;

    private Protocol(MethodHandle[] inboundPacketConstructors) {
        this.inboundPacketConstructors = inboundPacketConstructors;
    }

    public static Builder builder() {
        return new Builder();
    }

    public InboundPacket createInboundPacket(int packetId, ByteBuf buffer) throws Throwable {

        if (packetId < 0 || packetId >= inboundPacketConstructors.length) {
            return null;
        }
        MethodHandle constructor = inboundPacketConstructors[packetId];

        if (constructor == null) {
            return null;
        }
        return (InboundPacket) constructor.invokeExact(buffer);
    }

    public static class Builder {

        private MethodHandle[] inboundPacketFactories = new MethodHandle[0];

        public <P extends InboundPacket> Builder register(Class<P> packetClass) {
            var packet = packetClass.getAnnotation(Packet.class);

            if (packet == null) {
                LOGGER.warn("InboundPacket class {} is missing the @Packet annotation, skipping registration", packetClass.getSimpleName());
                return this;
            }
            try {
                if (packet.id() >= inboundPacketFactories.length) {
                    inboundPacketFactories = Arrays.copyOf(inboundPacketFactories, packet.id() + 1);
                }
                MethodHandle factoryHandle = LOOKUP.findStatic(packetClass, FACTORY_METHOD_NAME, FACTORY_METHOD_TYPE);

                inboundPacketFactories[packet.id()] = factoryHandle;
            } catch (NoSuchMethodException | IllegalAccessException e) {
                throw new IllegalArgumentException("The class " + packetClass.getSimpleName() + " does not have static factory method named 'decode' that accepts a ByteBuf", e);
            }
            return this;
        }

        public Protocol build() {
            return new Protocol(inboundPacketFactories);
        }
    }
}
