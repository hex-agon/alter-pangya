package work.fking.pangya.login.packet.handler;

import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.fking.pangya.login.networking.ConnectionState;
import work.fking.pangya.login.packet.inbound.SetNicknamePacket;
import work.fking.pangya.networking.protocol.InboundPacketHandler;

import javax.inject.Singleton;

@Singleton
public class SetNicknamePacketHandler implements InboundPacketHandler<SetNicknamePacket> {

    private static final Logger LOGGER = LogManager.getLogger(SetNicknamePacketHandler.class);

    @Override
    public void handle(Channel channel, SetNicknamePacket packet) {
        ConnectionState state = channel.attr(ConnectionState.KEY).get();

        if (state != ConnectionState.SELECTED_NICKNAME) {
            LOGGER.warn("Unexpected login session state, got={}, expected=SELECTED_NICKNAME", state);
            channel.disconnect();
        }
    }
}
