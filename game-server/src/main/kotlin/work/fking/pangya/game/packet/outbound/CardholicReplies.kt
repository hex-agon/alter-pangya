package work.fking.pangya.game.packet.outbound

import work.fking.pangya.game.player.CardInventory
import work.fking.pangya.networking.protocol.OutboundPacket

object CardholicReplies {

    fun inventory(cards: CardInventory): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x138)

            buffer.writeIntLE(0)
            buffer.writeShortLE(cards.entries.size)

            cards.entries.forEach { it.encode(buffer) }
        }
    }
}