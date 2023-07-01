package work.fking.pangya.game.packet.outbound

import io.netty.buffer.ByteBuf
import work.fking.pangya.game.model.IffContainer
import work.fking.pangya.game.model.IffObject
import work.fking.pangya.networking.protocol.OutboundPacket

class IffContainerChunkPacket<I : IffObject>(
    private val packetId: Int,
    private val totalEntries: Int,
    private val entriesChunk: List<I>
) : OutboundPacket {

    override fun encode(buffer: ByteBuf) {
        buffer.writeShortLE(packetId)
        buffer.writeShortLE(totalEntries)
        buffer.writeShortLE(entriesChunk.size)

        for (entry in entriesChunk) {
            entry.encode(buffer)
        }
    }

    companion object {
        private const val CHUNK_SIZE = 50
        fun <I : IffObject> chunk(packetId: Int, container: IffContainer<I>): List<IffContainerChunkPacket<I>> {

            return container.entries
                .chunked(CHUNK_SIZE)
                .map { IffContainerChunkPacket(packetId, container.entries.size, it) }
        }
    }
}
