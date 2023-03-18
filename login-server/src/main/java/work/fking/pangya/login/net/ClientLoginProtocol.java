package work.fking.pangya.login.net;

public class ClientLoginProtocol {

    private static final int PACKETS_SIZE = 512;
    private final ClientLoginPacketType[] packets;
    private final ClientLoginPacketHandler[] handlers;

    private ClientLoginProtocol(ClientLoginPacketType[] packets, ClientLoginPacketHandler[] handlers) {
        this.packets = packets;
        this.handlers = handlers;
    }

    public static ClientLoginProtocol create(ClientLoginPacketType[] packets) {
        var packetMap = new ClientLoginPacketType[PACKETS_SIZE];
        var handlers = new ClientLoginPacketHandler[PACKETS_SIZE];

        for (var packet : packets) {
            packetMap[packet.id()] = packet;
            handlers[packet.id()] = packet.handler();
        }
        return new ClientLoginProtocol(packetMap, handlers);
    }

    public ClientLoginPacketHandler[] handlers() {
        return handlers;
    }

    public ClientLoginPacketType forId(int packetId) {
        if (packetId < 0 || packetId >= PACKETS_SIZE) {
            return null;
        }
        return packets[packetId];
    }
}
