package work.fking.pangya.game.packet.outbound

import work.fking.pangya.networking.protocol.OutboundPacket

object LobbyResponses {
    fun ackLeave(): OutboundPacket {
        return OutboundPacket { buffer -> buffer.writeShortLE(0xf6) }
    }
}
