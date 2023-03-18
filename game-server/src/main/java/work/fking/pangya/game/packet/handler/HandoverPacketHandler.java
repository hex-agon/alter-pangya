package work.fking.pangya.game.packet.handler;

import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import work.fking.pangya.game.net.ClientGamePacketHandler;
import work.fking.pangya.game.GameServer;
import work.fking.pangya.game.Player;
import work.fking.pangya.game.packet.outbound.CaddieRosterPacket;
import work.fking.pangya.game.packet.outbound.CharacterRosterPacket;
import work.fking.pangya.game.packet.outbound.CookieBalancePacket;
import work.fking.pangya.game.packet.outbound.EquipmentPacket;
import work.fking.pangya.game.packet.outbound.HandoverReplies;
import work.fking.pangya.game.packet.outbound.InventoryPacket;
import work.fking.pangya.game.packet.outbound.MascotRosterPacket;
import work.fking.pangya.game.packet.outbound.PangBalancePacket;
import work.fking.pangya.game.packet.outbound.ServerChannelsPacket;
import work.fking.pangya.networking.protocol.ProtocolUtils;

public class HandoverPacketHandler implements ClientGamePacketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(HandoverPacketHandler.class);

    @Override
    public void handle(GameServer server, Player player, ByteBuf buffer) {
        var username = ProtocolUtils.readPString(buffer);
        int userId = buffer.readIntLE();
        buffer.readIntLE();
        buffer.readShortLE();
        var loginKey = ProtocolUtils.readPString(buffer);
        var clientVersion = ProtocolUtils.readPString(buffer);
        buffer.readIntLE(); // check c https://github.com/hsreina/pangya-server/blob/449140f97592d5d403ef0df01d19ca2c6c834361/src/Server/Sync/SyncServer.pas#L411
        buffer.readIntLE();
        String sessionKey = ProtocolUtils.readPString(buffer);

        LOGGER.info("Player {} handover with loginKey={}, sessionKey={}", username, loginKey, sessionKey);

        // https://github.com/hsreina/pangya-server/blob/449140f97592d5d403ef0df01d19ca2c6c834361/src/Server/Sync/SyncServer.pas#L430

        var channel = player.channel();
        channel.write(HandoverReplies.ok());
        channel.writeAndFlush(HandoverReplies.playerData());
        channel.writeAndFlush(new CharacterRosterPacket()); // known ok
        channel.writeAndFlush(new CaddieRosterPacket()); // known ok
        channel.writeAndFlush(new EquipmentPacket()); // known ok
        channel.writeAndFlush(new MascotRosterPacket());
        channel.writeAndFlush(new InventoryPacket());
        channel.writeAndFlush(new CookieBalancePacket()); // known ok
        channel.writeAndFlush(new PangBalancePacket()); // known ok
        channel.writeAndFlush(new ServerChannelsPacket());
    }
}
