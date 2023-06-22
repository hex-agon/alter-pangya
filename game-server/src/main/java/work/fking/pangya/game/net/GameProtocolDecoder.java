package work.fking.pangya.game.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.fking.pangya.networking.crypt.PangCrypt;

import java.util.List;

public class GameProtocolDecoder extends ByteToMessageDecoder {

    private static final Logger LOGGER = LogManager.getLogger(GameProtocolDecoder.class);

    private final ClientGameProtocol protocol;
    private final int cryptKey;

    private GameProtocolDecoder(ClientGameProtocol protocol, int cryptKey) {
        this.protocol = protocol;
        this.cryptKey = cryptKey;
    }

    public static GameProtocolDecoder create(ClientGameProtocol protocol, int cryptKey) {
        return new GameProtocolDecoder(protocol, cryptKey);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) {
        PangCrypt.decrypt(buffer, cryptKey);

        int packetId = buffer.readShortLE();
        LOGGER.trace("Incoming packetId=0x{}", Integer.toHexString(packetId));

        var packetType = protocol.forId(packetId);

        if (packetType == null) {
            LOGGER.warn("Unknown packetId=0x{}", Integer.toHexString(packetId));
            LOGGER.warn("\n{}", ByteBufUtil.prettyHexDump(buffer));
            ctx.disconnect();
            buffer.clear();
            return;
        }
        out.add(new ClientGamePacket(packetType, buffer.readRetainedSlice(buffer.readableBytes())));
    }
}
