package work.fking.pangya.networking.protocol

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import org.apache.logging.log4j.LogManager
import work.fking.pangya.networking.crypt.PangCrypt
import dev.pangya.lzo.MiniLZO

class ProtocolEncoder(
    private val cryptKey: Int
) : MessageToByteEncoder<OutboundPacket>() {
    private val logger = LogManager.getLogger(ProtocolEncoder::class.java)
    private val lzoOutBuffer = ByteArray(32768)
    private val lzoDict = IntArray(1 shl 14)

    override fun encode(ctx: ChannelHandlerContext, packet: OutboundPacket, buffer: ByteBuf) {
        logger.debug("Encoding packet={}", packet.javaClass.simpleName)
        // TODO: Everything here is terribly inefficient
        val pktBuffer = ctx.alloc().buffer()
        try {
            packet.encode(pktBuffer)
            val uncompressedSize = pktBuffer.readableBytes()
            val source = ByteArray(uncompressedSize)
            pktBuffer.readBytes(source)
            val compressedSize = MiniLZO.lzo1x_compress(source, source.size, lzoOutBuffer, lzoDict)
            val compressed = Unpooled.wrappedBuffer(lzoOutBuffer, 0, compressedSize)
            try {
                PangCrypt.encrypt(buffer, compressed, uncompressedSize, cryptKey, 0)
            } finally {
                compressed.release()
            }
        } finally {
            pktBuffer.release()
        }
    }
}
