package work.fking.pangya.game.task;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import work.fking.pangya.game.GameServer;
import work.fking.pangya.game.SessionClient.SessionInfo;
import work.fking.pangya.game.net.ClientPacketDispatcher;
import work.fking.pangya.game.net.ClientPacketType;
import work.fking.pangya.game.net.ClientProtocol;
import work.fking.pangya.game.net.GameProtocolDecoder;
import work.fking.pangya.game.packet.outbound.CookieBalancePacket;
import work.fking.pangya.game.packet.outbound.EquipmentPacket;
import work.fking.pangya.game.packet.outbound.HandoverReplies;
import work.fking.pangya.game.packet.outbound.HandoverReplies.HandoverResult;
import work.fking.pangya.game.packet.outbound.IffContainerChunkPacket;
import work.fking.pangya.game.packet.outbound.MascotRosterPacket;
import work.fking.pangya.game.packet.outbound.PangBalancePacket;
import work.fking.pangya.game.packet.outbound.ServerChannelsPacket;
import work.fking.pangya.game.packet.outbound.TreasureHunterPacket;

public class HandoverTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(HandoverTask.class);
    private static final ClientProtocol PROTOCOL = ClientProtocol.create(ClientPacketType.values());

    private final GameServer gameServer;
    private final Channel channel;
    private final int cryptKey;
    private final String sessionKey;

    public HandoverTask(GameServer gameServer, Channel channel, int cryptKey, String sessionKey) {
        this.gameServer = gameServer;
        this.channel = channel;
        this.cryptKey = cryptKey;
        this.sessionKey = sessionKey;
    }

    @Override
    public void run() {
        SessionInfo sessionInfo;
        try {
            sessionInfo = gameServer.sessionClient().loadSession(sessionKey);
        } catch (Exception e) {
            LOGGER.warn("Handover error sessionKey={}, message={}", sessionKey, e.getMessage());
            channel.writeAndFlush(HandoverReplies.error(HandoverResult.CANNOT_CONNECT_LOGIN_SERVER));
            return;
        }

        if (sessionInfo == null) {
            LOGGER.warn("Handover failed, unknown sessionKey={}", sessionKey);
            channel.writeAndFlush(HandoverReplies.error(HandoverResult.CANNOT_CONNECT_LOGIN_SERVER));
            return;
        }
        channel.write(HandoverReplies.ok());

        // load all the player stuff
        for (int i = 1; i <= 15; i++) {
            try {
                // do some fancy fake loading
                channel.writeAndFlush(HandoverReplies.updateProgressBar(i));
                Thread.sleep(25);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        // if no error, register it
        var player = gameServer.registerPlayer(channel, sessionInfo.uid(), sessionInfo.nickname());

        // modify the pipeline
        var pipeline = channel.pipeline();
        pipeline.remove("handoverHandler");
        pipeline.addLast("decoder", GameProtocolDecoder.create(PROTOCOL, cryptKey));
        pipeline.addLast("packetDispatcher", ClientPacketDispatcher.create(gameServer, player, PROTOCOL.handlers()));

        channel.write(HandoverReplies.handoverReply(player));
        IffContainerChunkPacket.create(0x70, player.characterRoster()).forEach(channel::write);
        IffContainerChunkPacket.create(0x71, player.caddieRoster()).forEach(channel::write);
        channel.write(EquipmentPacket.create(player));
        IffContainerChunkPacket.create(0x73, player.inventory()).forEach(channel::write);
        channel.write(new TreasureHunterPacket());
        channel.write(new MascotRosterPacket());
        channel.write(CookieBalancePacket.create(player));
        channel.write(PangBalancePacket.create(player));
        channel.writeAndFlush(new ServerChannelsPacket());
    }
}
