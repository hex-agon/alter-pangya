package work.fking.pangya.game.packet.outbound

import work.fking.pangya.game.player.Item
import work.fking.pangya.game.player.write
import work.fking.pangya.networking.protocol.OutboundPacket
import java.time.Instant

object ClubSetReplies {

    fun upgradeAck(result: UpgradeResult, stat: Int, itemUid: Int, cost: Long = 0): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0xa5)
            buffer.writeByte(result.code)
            buffer.writeByte(1) // 0 doesn't upgrade a clubset but 1 does?
            buffer.writeByte(stat)
            buffer.writeIntLE(itemUid)
            buffer.writeLongLE(cost)
        }
    }

    fun workshopRankUpTransform(): OutboundPacket {
        return OutboundPacket { buffer -> buffer.writeShortLE(0x241) }
    }

    fun workshopRankUpAck(clubSetUid: Int, leveledUpStat: Int): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x240)
            buffer.writeIntLE(0) // result 0 = ok, 1 = 'System error'

            buffer.writeIntLE(leveledUpStat) // which stat leveled up
            buffer.writeIntLE(clubSetUid)
        }
    }

    fun syncClubSetItem(item: Item, statBefore: Int, statAfter: Int): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x216)

            buffer.writeIntLE(Instant.now().epochSecond.toInt())
            buffer.writeIntLE(1)

            buffer.writeByte(0x2) // this packet can sync multiple things, 0x2 means it's an inventory item
            buffer.writeIntLE(item.iffId)
            buffer.writeIntLE(item.uid)
            buffer.writeIntLE(0)
            buffer.writeIntLE(statBefore)
            buffer.writeIntLE(statAfter)
            buffer.writeIntLE(item.quantity)
            buffer.writeZero(25)
            buffer.write(item.clubWorkshop)
        }
    }

    enum class UpgradeResult(val code: Int) {
        UPGRADE_SUCCESS(1),
        DOWNGRADE_SUCCESS(2),
        INSUFFICIENT_PANG(3),
        INSUFFICIENT_SLOTS(4),
        CANNOT_DOWNGRADE_ANYMORE(5),
        FAILED_TO_UPGRADE(6)
    }
}