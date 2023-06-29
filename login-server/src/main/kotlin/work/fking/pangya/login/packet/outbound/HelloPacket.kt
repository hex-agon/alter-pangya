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

    override fun encode(target: ByteBuf) {
        target.writeShortLE(0xb00)
        target.writeZero(4)
        target.writeByte(cryptKey)
        target.writeZero(3)
        target.writeIntLE(10101) // suspicious value, similar to ports used by login servers
    }
}
