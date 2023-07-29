package work.fking.pangya.networking.protocol

import dev.pangya.lzo.MiniLZO
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import org.apache.logging.log4j.LogManager
import work.fking.pangya.networking.crypt.PangCrypt
import java.util.Arrays
import kotlin.math.max

class ProtocolEncoder(
    private val cryptKey: Int
) : MessageToByteEncoder<OutboundPacket>() {
    private val logger = LogManager.getLogger(ProtocolEncoder::class.java)
    private val lzoDict = IntArray(1 shl 14)

    override fun encode(ctx: ChannelHandlerContext, packet: OutboundPacket, buffer: ByteBuf) {
        // TODO: Everything here is terribly inefficient
        val pktBuffer = ctx.alloc().buffer()
        try {
            packet.encode(pktBuffer)
            logger.debug("Encoding packet=0x{}", Integer.toHexString(pktBuffer.getShortLE(0).toInt()))
            val uncompressedSize = pktBuffer.readableBytes()
            val source = ByteArray(uncompressedSize)
            pktBuffer.readBytes(source)

            Arrays.fill(lzoDict, 0)
            val compressedBuffer = ByteArray(source.size + 16 * max(1, source.size / 1024))
            val compressedSize = MiniLZO.lzo1x_compress(source, source.size, compressedBuffer, lzoDict)
            val compressed = Unpooled.wrappedBuffer(compressedBuffer, 0, compressedSize)
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
