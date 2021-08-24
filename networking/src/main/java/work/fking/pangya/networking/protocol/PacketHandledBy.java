package work.fking.pangya.networking.protocol;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PacketHandledBy {

    Class<? extends InboundPacketHandler<? extends InboundPacket>> value();
}
