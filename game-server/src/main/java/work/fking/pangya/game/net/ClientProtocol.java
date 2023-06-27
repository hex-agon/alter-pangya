package work.fking.pangya.game.net;

public class ClientProtocol {

    private static final int PACKETS_SIZE = 512;
    private final ClientPacketType[] packets;
    private final ClientPacketHandler[] handlers;

    private ClientProtocol(ClientPacketType[] packets, ClientPacketHandler[] handlers) {
        this.packets = packets;
        this.handlers = handlers;
    }

    public static ClientProtocol create(ClientPacketType[] packets) {
        var packetMap = new ClientPacketType[PACKETS_SIZE];
        var handlers = new ClientPacketHandler[PACKETS_SIZE];

        for (var packet : packets) {
            packetMap[packet.id()] = packet;
            handlers[packet.id()] = packet.handler();
        }
        return new ClientProtocol(packetMap, handlers);
    }

    public ClientPacketHandler[] handlers() {
        return handlers;
    }

    public ClientPacketType forId(int packetId) {
        if (packetId < 0 || packetId >= PACKETS_SIZE) {
            return null;
        }
        return packets[packetId];
    }
}
