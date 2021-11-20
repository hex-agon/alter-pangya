package work.fking.pangya.networking.protocol;

public interface InboundPacket {

    default int id() {
        return getClass().getAnnotation(Packet.class).id();
    }

    default Class<? extends InboundPacketHandler<? extends InboundPacket>> handledBy() {
        return getClass().getAnnotation(Packet.class).handledBy();
    }
}
