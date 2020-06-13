package work.fking.pangya.networking.protocol;

public interface InboundPacketHandlerFactory {

    <P extends InboundPacket> InboundPacketHandler<P> create(Class<? extends InboundPacketHandler<P>> packetHandlerClass);
}
