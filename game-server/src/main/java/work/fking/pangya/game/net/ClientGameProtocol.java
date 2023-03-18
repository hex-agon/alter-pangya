package work.fking.pangya.game.net;

public class ClientGameProtocol {

    private static final int PACKETS_SIZE = 512;
    private final ClientGamePacketType[] packets;
    private final ClientGamePacketHandler[] handlers;

    private ClientGameProtocol(ClientGamePacketType[] packets, ClientGamePacketHandler[] handlers) {
        this.packets = packets;
        this.handlers = handlers;
    }

    public static ClientGameProtocol create(ClientGamePacketType[] packets) {
        var packetMap = new ClientGamePacketType[PACKETS_SIZE];
        var handlers = new ClientGamePacketHandler[PACKETS_SIZE];

        for (var packet : packets) {
            packetMap[packet.id()] = packet;
            handlers[packet.id()] = packet.handler();
        }
        return new ClientGameProtocol(packetMap, handlers);
    }

    public ClientGamePacketHandler[] handlers() {
        return handlers;
    }

    public ClientGamePacketType forId(int packetId) {
        if (packetId < 0 || packetId >= PACKETS_SIZE) {
            return null;
        }
        return packets[packetId];
    }
}
