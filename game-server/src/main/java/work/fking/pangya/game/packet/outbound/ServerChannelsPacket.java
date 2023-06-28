package work.fking.pangya.game.packet.outbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.game.ServerChannel;
import work.fking.pangya.game.ServerChannel.Restriction;
import work.fking.pangya.networking.protocol.OutboundPacket;

import java.util.List;

import static work.fking.pangya.networking.protocol.ProtocolUtils.writeFixedSizeString;

public class ServerChannelsPacket implements OutboundPacket {

    private static final int ID = 0x4d;

    private final List<ServerChannel> serverChannels;

    public ServerChannelsPacket(List<ServerChannel> serverChannels) {
        this.serverChannels = serverChannels;
    }

    public static ServerChannelsPacket create(List<ServerChannel> serverChannels) {
        return new ServerChannelsPacket(serverChannels);
    }

    @Override
    public void encode(ByteBuf target) {
        target.writeShortLE(ID);
        target.writeByte(serverChannels.size());

        for (var channel : serverChannels) {
            writeFixedSizeString(target, channel.name(), 64);
            target.writeShortLE(channel.capacity());
            target.writeShortLE(channel.playerCount());
            target.writeShortLE(channel.id());
            target.writeShortLE(pack(channel.restrictions()));
            target.writeZero(5);
        }
    }

    private int pack(List<Restriction> restrictions) {
        var bitFlags = 0;
        for (var restriction : restrictions) {
            bitFlags |= restriction.flag();
        }
        return bitFlags;
    }
}
