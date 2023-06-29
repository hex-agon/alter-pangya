package work.fking.pangya.login.packet.outbound

import work.fking.pangya.networking.protocol.OutboundPacket

object ConfirmCharacterReplies {

    fun ok(): OutboundPacket {
        return OutboundPacket { buffer ->
            buffer.writeShortLE(0x11)
            buffer.writeByte(0)
        }
    }
}
