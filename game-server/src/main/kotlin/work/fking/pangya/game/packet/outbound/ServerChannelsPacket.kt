package work.fking.pangya.game.packet.outbound

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.ServerChannel
import work.fking.pangya.game.ServerChannel.Restriction
import work.fking.pangya.networking.protocol.OutboundPacket
import work.fking.pangya.networking.protocol.writeFixedSizeString

class ServerChannelsPacket(
    private val serverChannels: List<ServerChannel>
) : OutboundPacket {

    override fun encode(buffer: ByteBuf) {
        buffer.writeShortLE(0x4d)
        buffer.writeByte(serverChannels.size)

        for (channel in serverChannels) {
            buffer.writeFixedSizeString(channel.name, 64)
            buffer.writeShortLE(channel.capacity)
            buffer.writeShortLE(channel.playerCount())
            buffer.writeShortLE(channel.id)
            buffer.writeShortLE(pack(channel.restrictions))
            buffer.writeZero(5)
        }
    }

    private fun pack(restrictions: List<Restriction>): Int {
        var bitFlags = 0
        for (restriction in restrictions) {
            bitFlags = bitFlags or restriction.flag()
        }
        return bitFlags
    }
}
