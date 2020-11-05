package work.fking.pangya.networking.protocol;

/**
 * Marker interface for network inbound packets.
 * In order to be registered with a {@link Protocol}, classes must provide a factory method that accepts a {@link io.netty.buffer.ByteBuf}
 */
public interface InboundPacket {

}
