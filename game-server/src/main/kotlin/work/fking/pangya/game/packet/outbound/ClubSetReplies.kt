package work.fking.pangya.game.packet.outbound

import work.fking.pangya.networking.protocol.OutboundPacket

object ClubSetReplies {

    fun upgradeAck(type: Int, stat: Int, itemUid: Int, cost: Long): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0xa5)
            buffer.writeByte(type)
            buffer.writeByte(1) // 0 doesn't upgrade a clubset but 1 does?
            buffer.writeByte(stat)
            buffer.writeIntLE(itemUid)
            buffer.writeLongLE(cost) // TODO: how to calculate the upgrade cost?
        }
    }
}