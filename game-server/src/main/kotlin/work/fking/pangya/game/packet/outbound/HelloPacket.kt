package work.fking.pangya.game.packet.outbound

import io.netty.buffer.ByteBuf
import work.fking.pangya.networking.crypt.PangCrypt
import work.fking.pangya.networking.protocol.OutboundPacket

class HelloPacket(
    private val cryptKey: Int
) : OutboundPacket {

    init {
        require(!(cryptKey < 0 || cryptKey > PangCrypt.CRYPT_KEY_MAX)) { "Invalid crypt key" }
    }

    private val unknown = byteArrayOf(0x06, 0x00, 0x00, 0x3F, 0x00, 0x01, 0x01)

    override fun encode(target: ByteBuf) {
        target.writeByte(0)
        target.writeBytes(unknown)
        target.writeByte(cryptKey)
    }
}
