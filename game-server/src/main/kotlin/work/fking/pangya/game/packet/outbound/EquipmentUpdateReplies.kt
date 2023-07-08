package work.fking.pangya.game.packet.outbound

import work.fking.pangya.game.player.Caddie
import work.fking.pangya.game.player.Character
import work.fking.pangya.game.player.Item
import work.fking.pangya.networking.protocol.OutboundPacket

object EquipmentUpdateReplies {

    fun equipCharacterPartsAck(character: Character): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x6b)
            buffer.writeByte(4)
            buffer.writeByte(0)
            character.encode(buffer)
        }
    }

    fun equipCaddieAck(caddie: Caddie): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x6b)
            buffer.writeByte(4)
            buffer.writeByte(1)
            buffer.writeIntLE(caddie.uid)
        }
    }

    fun equipItemsAck(equippedItems: IntArray): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x6b)
            buffer.writeByte(4) // result code, 4 = 'ok'
            buffer.writeByte(2) // equipment ack type
            equippedItems.forEach { buffer.writeIntLE(it) }
        }
    }

    fun equipCometAck(comet: Item): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x6b)
            buffer.writeByte(4)
            buffer.writeByte(3)
            buffer.writeIntLE(comet.iffId)
        }
    }

    fun equipCharacterAck(character: Character): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x6b)
            buffer.writeByte(4)
            buffer.writeByte(5)
            buffer.writeIntLE(character.uid)
        }
    }
}