package work.fking.pangya.networking.protocol

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder

class SimplePacketEncoder : MessageToByteEncoder<OutboundPacket>() {
    override fun encode(ctx: ChannelHandlerContext, packet: OutboundPacket, buffer: ByteBuf) {
        packet.encode(buffer)
    }
}
