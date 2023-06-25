package work.fking.pangya.game.task;

import io.netty.channel.Channel;
import work.fking.pangya.game.GameServer;
import work.fking.pangya.game.net.ClientGamePacketDispatcher;
import work.fking.pangya.game.net.ClientGamePacketType;
import work.fking.pangya.game.net.ClientGameProtocol;
import work.fking.pangya.game.net.GameProtocolDecoder;
import work.fking.pangya.game.packet.outbound.CookieBalancePacket;
import work.fking.pangya.game.packet.outbound.EquipmentPacket;
import work.fking.pangya.game.packet.outbound.HandoverReplies;
import work.fking.pangya.game.packet.outbound.IffContainerChunkPacket;
import work.fking.pangya.game.packet.outbound.MascotRosterPacket;
import work.fking.pangya.game.packet.outbound.PangBalancePacket;
import work.fking.pangya.game.packet.outbound.ServerChannelsPacket;
import work.fking.pangya.game.packet.outbound.TreasureHunterPacket;

public class LoginTask implements Runnable {

    private static final ClientGameProtocol PROTOCOL = ClientGameProtocol.create(ClientGamePacketType.values());

    private final GameServer gameServer;
    private final Channel channel;
    private final int cryptKey;

    public LoginTask(GameServer gameServer, Channel channel, int cryptKey) {
        this.gameServer = gameServer;
        this.channel = channel;
        this.cryptKey = cryptKey;
    }

    @Override
    public void run() {
        channel.write(HandoverReplies.ok());

        // load all the player stuff
        for (int i = 1; i <= 15; i++) {
            try {
                // do some fancy fake loading
                channel.writeAndFlush(HandoverReplies.updateProgressBar(i));
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        // if no error, register it
        var player = gameServer.registerPlayer(channel);

        // modify the pipeline
        var pipeline = channel.pipeline();
        pipeline.remove("handoverHandler");
        pipeline.addLast("decoder", GameProtocolDecoder.create(PROTOCOL, cryptKey));
        pipeline.addLast("packetDispatcher", ClientGamePacketDispatcher.create(gameServer, player, PROTOCOL.handlers()));

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
