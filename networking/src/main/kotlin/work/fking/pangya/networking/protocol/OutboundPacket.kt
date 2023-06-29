package work.fking.pangya.networking.protocol

import io.netty.buffer.ByteBuf

fun interface OutboundPacket {
    fun encode(buffer: ByteBuf)
}
