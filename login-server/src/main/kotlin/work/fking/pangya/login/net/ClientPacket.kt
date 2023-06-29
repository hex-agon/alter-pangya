package work.fking.pangya.login.net

import io.netty.buffer.ByteBuf

@JvmRecord
data class ClientPacket(val type: ClientPacketType, val buffer: ByteBuf)
