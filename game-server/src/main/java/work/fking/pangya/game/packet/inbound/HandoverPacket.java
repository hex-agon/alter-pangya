package work.fking.pangya.game.packet.inbound;

import io.netty.buffer.ByteBuf;
import work.fking.pangya.game.packet.handler.HandoverPacketHandler;
import work.fking.pangya.networking.protocol.InboundPacket;
import work.fking.pangya.networking.protocol.PacketHandledBy;
import work.fking.pangya.networking.protocol.PacketId;
import work.fking.pangya.networking.protocol.ProtocolUtils;

@PacketId(0x2)
@PacketHandledBy(HandoverPacketHandler.class)
public record HandoverPacket(String username, int userId, String loginKey, String clientVersion, String sessionKey) implements InboundPacket {

    public static InboundPacket decode(ByteBuf buffer) {
        String username = ProtocolUtils.readPString(buffer);
        int userId = buffer.readIntLE();
        buffer.readIntLE();
        buffer.readShortLE();
        String loginKey = ProtocolUtils.readPString(buffer);
        String clientVersion = ProtocolUtils.readPString(buffer);
        buffer.readIntLE(); // check c https://github.com/hsreina/pangya-server/blob/449140f97592d5d403ef0df01d19ca2c6c834361/src/Server/Sync/SyncServer.pas#L411
        buffer.readIntLE();
        String sessionKey = ProtocolUtils.readPString(buffer);

        return new HandoverPacket(username, userId, loginKey, clientVersion, sessionKey);
    }
}
