package work.fking.pangya.login.net

class ClientProtocol(
    packetTypes: Array<ClientPacketType>
) {

    companion object {
        private const val PACKETS_SIZE = 512
    }

    private val packets = arrayOfNulls<ClientPacketType>(PACKETS_SIZE)
    private val handlers = arrayOfNulls<ClientPacketHandler>(PACKETS_SIZE)

    init {
        for (packet in packetTypes) {
            packets[packet.id()] = packet
            handlers[packet.id()] = packet.handler()
        }
    }

    fun handlers(): Array<ClientPacketHandler?> {
        return handlers
    }

    fun forId(packetId: Int): ClientPacketType? {
        return if (packetId < 0 || packetId >= PACKETS_SIZE) {
            null
        } else packets[packetId]
    }

}
