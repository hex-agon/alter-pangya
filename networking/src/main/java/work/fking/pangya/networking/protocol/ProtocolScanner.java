package work.fking.pangya.networking.protocol;

import net.jodah.typetools.TypeResolver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import work.fking.pangya.networking.protocol.InboundPacketDispatcher.InboundPacketHandlerFactory;

/**
 * A class that automatically scans the project for inbound packets and creates the {@link Protocol} and {@link InboundPacketDispatcher} automatically.
 */
public class ProtocolScanner {

    private static final Logger LOGGER = LogManager.getLogger(ProtocolScanner.class);

    private ProtocolScanner() {
    }

    public static ScanResult scan(InboundPacketHandlerFactory handlerFactory) {
        LOGGER.debug("Scanning the classpath...");
        Reflections reflections = new Reflections("work.fking.pangya");
        var protocolBuilder = Protocol.builder();
        var dispatcherBuilder = InboundPacketDispatcher.builder(handlerFactory);
        var packetFactories = reflections.getSubTypesOf(InboundPacket.class);

        for (Class<? extends InboundPacket> inboundPacket : packetFactories) {
            var packetId = inboundPacket.getAnnotation(PacketId.class);
            var packetHandlerAnnotation = inboundPacket.getAnnotation(PacketHandledBy.class);

            if (packetId == null) {
                LOGGER.warn("InboundPacket class {} is missing the @PacketId annotation, skipping registration", inboundPacket.getSimpleName());
                continue;
            }
            if (packetHandlerAnnotation == null) {
                LOGGER.warn("InboundPacket class {} is missing the @PacketHandledBy annotation, skipping registration", inboundPacket.getSimpleName());
                continue;
            }
            var packetHandler = packetHandlerAnnotation.value();
            var actualPacketType = TypeResolver.resolveRawArgument(InboundPacketHandler.class, packetHandler);

            if (!inboundPacket.equals(actualPacketType)) {
                LOGGER.warn("Unexpected packet type in handler {}, expected {}, got {}", packetHandler.getSimpleName(), inboundPacket.getSimpleName(), actualPacketType.getSimpleName());
                continue;
            }
            LOGGER.trace("Registering packet {} with id {} and handler {}", inboundPacket.getSimpleName(), packetId.value(), packetHandlerAnnotation.value().getSimpleName());
            protocolBuilder.inboundPacket(packetId.value(), inboundPacket);
            dispatcherBuilder.handler(inboundPacket, packetHandler);
        }
        return new ScanResult(protocolBuilder.build(), dispatcherBuilder.build());
    }

    public static record ScanResult(Protocol protocol, InboundPacketDispatcher packetDispatcher) {

    }
}
