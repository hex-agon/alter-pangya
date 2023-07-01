package work.fking.pangya.game.packet.outbound

import work.fking.pangya.networking.protocol.OutboundPacket

object LobbyReplies {

    fun ackJoin(): OutboundPacket {
        return OutboundPacket { buffer -> buffer.writeShortLE(0xf5) }
    }

    fun ackLeave(): OutboundPacket {
        return OutboundPacket { buffer -> buffer.writeShortLE(0xf6) }
    }
}
