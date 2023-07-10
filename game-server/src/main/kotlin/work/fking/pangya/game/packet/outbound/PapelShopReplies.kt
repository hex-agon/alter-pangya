package work.fking.pangya.game.packet.outbound

import work.fking.pangya.networking.protocol.OutboundPacket

object PapelShopReplies {

    fun success(): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x21B) // packetId
            buffer.writeIntLE(0) // result, 0 = ok, anything else results in a 'system error' message with this value printed as the code
            buffer.writeIntLE(3000) // something related to the coupon, likely unique item id, it seems like you can send anything != 0 and it'll deduct 1
            buffer.writeIntLE(3) // ball count

            // for each ball
            buffer.writeIntLE(0) // ball color (blue, green, red)
            buffer.writeIntLE(268435505) // item id
            buffer.writeIntLE(3486) // item unique id
            buffer.writeIntLE(1) // quantity
            buffer.writeIntLE(2) // item 'quality', 2 = red, 1 = blue, 0 = gray
            buffer.writeIntLE(1) // ball color (blue, green, red)
            buffer.writeIntLE(402653187) // item id
            buffer.writeIntLE(6578) // item unique id
            buffer.writeIntLE(1) // quantity
            buffer.writeIntLE(1) // item 'quality', 2 = red, 1 = blue, 0 = gray
            buffer.writeIntLE(2) // ball color (blue, green, red)
            buffer.writeIntLE(402653184) // item id
            buffer.writeIntLE(56467) // item unique id
            buffer.writeIntLE(1) // quantity
            buffer.writeIntLE(0) // item 'quality', 2 = red, 1 = blue, 0 = gray
            buffer.writeLongLE(10000) // pang balance after gamba
            buffer.writeLongLE(20000) // cookie balance after gamba
        }
    }
}
