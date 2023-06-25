package work.fking.pangya.game.packet.outbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.game.model.IffContainer;
import work.fking.pangya.game.model.IffObject;
import work.fking.pangya.networking.protocol.OutboundPacket;

import java.util.ArrayList;
import java.util.List;

public class IffContainerChunkPacket<I extends IffObject> implements OutboundPacket {

    private static final int CHUNK_SIZE = 50;

    private final int packetId;
    private final int totalEntries;
    private final List<I> entriesChunk;

    public IffContainerChunkPacket(int packetId, int totalEntries, List<I> entriesChunk) {
        this.packetId = packetId;
        this.totalEntries = totalEntries;
        this.entriesChunk = entriesChunk;
    }

    public static <I extends IffObject> List<IffContainerChunkPacket<I>> create(int packetId, IffContainer<I> container) {
        var totalEntries = container.size();
        var packets = new ArrayList<IffContainerChunkPacket<I>>(totalEntries / CHUNK_SIZE + 1);

        for (int i = 0; i < totalEntries; i += CHUNK_SIZE) {
            var chunk = container.chunk(i, CHUNK_SIZE);
            var packet = new IffContainerChunkPacket<>(packetId, totalEntries, chunk);
            packets.add(packet);
        }
        return packets;
    }

    @Override
    public void encode(ByteBuf buffer) {
        buffer.writeShortLE(packetId);

        buffer.writeShortLE(totalEntries);
        buffer.writeShortLE(entriesChunk.size());

        for (var entry : entriesChunk) {
            entry.encode(buffer);
        }
    }
}
