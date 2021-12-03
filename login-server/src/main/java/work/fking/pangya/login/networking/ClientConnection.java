package work.fking.pangya.login.networking;

import work.fking.pangya.networking.protocol.OutboundPacket;

public interface ClientConnection {

    boolean isAlive();

    void disconnect();

    void write(OutboundPacket packet);

    void flush();

    void writeAndFlush(OutboundPacket packet);
}
