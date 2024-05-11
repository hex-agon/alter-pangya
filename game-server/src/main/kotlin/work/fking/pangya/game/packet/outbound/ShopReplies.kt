package work.fking.pangya.game.packet.outbound

import work.fking.pangya.game.packet.outbound.PurchaseFailedReason.GENERIC_ERROR
import work.fking.pangya.game.player.PlayerWallet
import work.fking.pangya.networking.protocol.OutboundPacket

enum class PurchaseFailedReason(val code: Int) {
    GENERIC_ERROR(1),
    NOT_ENOUGH_PANG(2),
    WRONG_ITEM_CODE(3),
    ALREADY_HAVE_THAT_ITEM(4),
    CHECK_ITEM_TIME_LIMIT(9), // this is maybe when trying to rent an item that is already rented
    CHECK_SALE_TIMES(11)
}

object ShopReplies {

    fun purchaseFailed(reason: PurchaseFailedReason = GENERIC_ERROR): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x68)
            buffer.writeIntLE(reason.code) // 1 = purchase failed, 2 = not enough pang, 3 = wrong item code, 4 = already have that item, 9 = check time limit of your item, 11 = please check sale times
        }
    }

    fun purchaseSuccessful(wallet: PlayerWallet): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x68)
            buffer.writeIntLE(0)
            buffer.writeLongLE(wallet.pangBalance)
            buffer.writeLongLE(wallet.cookieBalance)
        }
    }

    fun shopUnknownReply(): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x20e)
            buffer.writeIntLE(0) // maybe this is a timestamp to sync sales
            buffer.writeIntLE(0)
        }
    }
}