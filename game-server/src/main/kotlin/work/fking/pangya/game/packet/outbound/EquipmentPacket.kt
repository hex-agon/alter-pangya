package work.fking.pangya.game.packet.outbound

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.player.Equipment
import work.fking.pangya.game.player.Player
import work.fking.pangya.networking.protocol.OutboundPacket

class EquipmentPacket(
    private val equipment: Equipment
) : OutboundPacket {

    constructor(player: Player) : this(player.equipment)

    override fun encode(buffer: ByteBuf) {
        buffer.writeShortLE(0x72)
        equipment.encode(buffer)
    }
}
