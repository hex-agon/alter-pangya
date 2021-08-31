package work.fking.pangya.login.packet.handler;

import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.fking.pangya.login.model.ConnectionState;
import work.fking.pangya.login.packet.inbound.CheckNicknamePacket;
import work.fking.pangya.login.packet.outbound.CheckNicknameReplies;
import work.fking.pangya.networking.protocol.InboundPacketHandler;

import javax.inject.Singleton;

@Singleton
public class CheckNicknamePacketHandler implements InboundPacketHandler<CheckNicknamePacket> {

    private static final Logger LOGGER = LogManager.getLogger(CheckNicknamePacketHandler.class);

    @Override
    public void handle(Channel channel, CheckNicknamePacket packet) {
        ConnectionState state = channel.attr(ConnectionState.KEY).get();

        if (state != ConnectionState.SELECTING_NICKNAME && state != ConnectionState.SELECTED_NICKNAME) {
            LOGGER.warn("Unexpected login session state, got={}, expected=SELECTING_NICKNAME or SELECTED_NICKNAME", state);
            channel.disconnect();
            return;
        }
        channel.writeAndFlush(CheckNicknameReplies.available(packet.nickname()));
    }
}
