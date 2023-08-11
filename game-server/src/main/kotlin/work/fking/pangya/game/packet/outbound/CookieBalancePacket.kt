package work.fking.pangya.game.packet.outbound

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.player.Player
import work.fking.pangya.networking.protocol.OutboundPacket

class CookieBalancePacket(
    private val balance: Long
) : OutboundPacket {

    constructor(player: Player) : this(player.wallet.cookieBalance)

    override fun encode(buffer: ByteBuf) {
        buffer.writeShortLE(0x96)
        buffer.writeLongLE(balance)
    }
}
