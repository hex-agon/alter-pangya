package work.fking.pangya.login.packet.outbound

import io.netty.buffer.ByteBuf
import work.fking.pangya.networking.crypt.PangCrypt
import work.fking.pangya.networking.protocol.OutboundPacket

class HelloPacket(
    private val cryptKey: Int
) : OutboundPacket {

    init {
        require(!(cryptKey < 0 || cryptKey > PangCrypt.CRYPT_KEY_MAX)) { "Crypt key out of bounds" }
    }

    override fun encode(buffer: ByteBuf) {
        buffer.writeShortLE(0xb00)
        buffer.writeZero(4)
        buffer.writeByte(cryptKey)
        buffer.writeZero(3)
        buffer.writeIntLE(10101) // suspicious value, similar to ports used by login servers
    }
}
