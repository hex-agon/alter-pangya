package work.fking.pangya.game.packet.outbound

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.ServerChannel
import work.fking.pangya.game.ServerChannel.Restriction
import work.fking.pangya.networking.protocol.OutboundPacket
import work.fking.pangya.networking.protocol.writeFixedSizeString

class ServerChannelsPacket(
    private val serverChannels: List<ServerChannel>
) : OutboundPacket {

    override fun encode(target: ByteBuf) {
        target.writeShortLE(0x4d)
        target.writeByte(serverChannels.size)

        for (channel in serverChannels) {
            target.writeFixedSizeString(channel.name(), 64)
            target.writeShortLE(channel.capacity())
            target.writeShortLE(channel.playerCount())
            target.writeShortLE(channel.id())
            target.writeShortLE(pack(channel.restrictions()))
            target.writeZero(5)
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
