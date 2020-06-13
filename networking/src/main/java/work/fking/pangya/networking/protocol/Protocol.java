package work.fking.pangya.networking.protocol;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.util.Arrays;

/**
 * A simple class representing a packet based protocol. This class also provides a factory method for the registered packets.
 */
public final class Protocol {

    private static final MethodType DEFAULT_CONSTRUCTOR_METHOD_TYPE = MethodType.methodType(void.class);
    private static final Lookup LOOKUP = MethodHandles.lookup();

    private MethodHandle[] inboundPacketConstructors = new MethodHandle[0];

    private Protocol() {
    }

    public static Protocol create() {
        return new Protocol();
    }

    public <P extends InboundPacket> Protocol registerInboundPacket(int packetId, Class<P> packetClass) {
        try {
            if (packetId >= inboundPacketConstructors.length) {
                inboundPacketConstructors = Arrays.copyOf(inboundPacketConstructors, packetId + 1);
            }
            MethodHandle constructorHandle = LOOKUP.findConstructor(packetClass, DEFAULT_CONSTRUCTOR_METHOD_TYPE);
            constructorHandle = constructorHandle.asType(constructorHandle.type().changeReturnType(InboundPacket.class));

            inboundPacketConstructors[packetId] = constructorHandle;
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new IllegalArgumentException("The packet class does not have a default zero args constructor", e);
        }
        return this;
    }

    public InboundPacket createInboundPacket(int packetId) throws Throwable {

        if (packetId < 0 || packetId >= inboundPacketConstructors.length) {
            return null;
        }
        MethodHandle constructor = inboundPacketConstructors[packetId];

        if (constructor == null) {
            return null;
        }
        return (InboundPacket) constructor.invokeExact();
    }
}
