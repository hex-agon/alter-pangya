package work.fking.pangya.game.packet.outbound

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.player.Player
import work.fking.pangya.networking.protocol.OutboundPacket

class PangBalancePacket(
    private val balance: Int
) : OutboundPacket {

    constructor(player: Player) : this(player.pangBalance())

    override fun encode(buffer: ByteBuf) {
        buffer.writeShortLE(0xc8)
        buffer.writeLongLE(balance.toLong())
    }
}
