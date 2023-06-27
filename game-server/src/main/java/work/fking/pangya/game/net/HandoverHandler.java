package work.fking.pangya.game.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import work.fking.pangya.game.GameServer;
import work.fking.pangya.game.task.HandoverTask;
import work.fking.pangya.networking.crypt.PangCrypt;

import static work.fking.pangya.networking.protocol.ProtocolUtils.readPString;

public class HandoverHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HandoverHandler.class);
    private static final int PACKET_ID = 0x2;

    private final GameServer gameServer;
    private final int cryptKey;

    private HandoverHandler(GameServer gameServer, int cryptKey) {
        this.gameServer = gameServer;
        this.cryptKey = cryptKey;
    }

    public static HandoverHandler create(GameServer gameServer, int cryptKey) {
        return new HandoverHandler(gameServer, cryptKey);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buffer) {
        PangCrypt.decrypt(buffer, cryptKey);
        int packetId = buffer.readShortLE();

        if (packetId != PACKET_ID) {
            LOGGER.warn("Unexpected packet during handover, packetId={}", packetId);
            ctx.disconnect();
            return;
        }
        var username = readPString(buffer);
        int uid = buffer.readIntLE();
        buffer.readIntLE();
        buffer.readShortLE();
        var loginKey = readPString(buffer);
        var clientVersion = readPString(buffer);
        buffer.readIntLE(); // check c https://github.com/hsreina/pangya-server/blob/449140f97592d5d403ef0df01d19ca2c6c834361/src/Server/Sync/SyncServer.pas#L411
        buffer.readIntLE();
        String sessionKey = readPString(buffer);

        LOGGER.info("Successful handover username={}, uid={}, clientVersion={}, loginKey={}, sessionKey={}", username, uid, clientVersion, loginKey, sessionKey);

        gameServer.submitTask(new HandoverTask(gameServer, ctx.channel(), cryptKey, sessionKey));
    }
}
