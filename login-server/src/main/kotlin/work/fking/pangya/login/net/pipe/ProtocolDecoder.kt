package work.fking.pangya.login.net.pipe

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufUtil
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import org.slf4j.LoggerFactory
import work.fking.pangya.login.net.ClientPacket
import work.fking.pangya.login.net.ClientProtocol
import work.fking.pangya.networking.crypt.PangCrypt

private val LOGGER = LoggerFactory.getLogger(ProtocolDecoder::class.java)

class ProtocolDecoder(
    private val protocol: ClientProtocol,
    private val cryptKey: Int
) : ByteToMessageDecoder() {

    override fun decode(ctx: ChannelHandlerContext, buffer: ByteBuf, out: MutableList<Any>) {
        PangCrypt.decrypt(buffer, cryptKey)
        val packetId = buffer.readShortLE().toInt()
        LOGGER.trace("Incoming packetId=0x{}", Integer.toHexString(packetId))
        val packetType = protocol.forId(packetId)

        if (packetType == null) {
            LOGGER.warn("Unknown packetId=0x{}", Integer.toHexString(packetId))
            LOGGER.warn("\n{}", ByteBufUtil.prettyHexDump(buffer))
            ctx.disconnect()
            buffer.clear()
            return
        }
        out.add(ClientPacket(packetType, buffer.readRetainedSlice(buffer.readableBytes())))
    }
}