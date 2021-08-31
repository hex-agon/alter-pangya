package work.fking.pangya.login.packet.handler;

import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.fking.pangya.login.model.ConnectionState;
import work.fking.pangya.login.packet.inbound.SelectCharacterPacket;
import work.fking.pangya.networking.protocol.InboundPacketHandler;

import javax.inject.Singleton;

@Singleton
public class SelectCharacterPacketHandler implements InboundPacketHandler<SelectCharacterPacket> {

    private static final Logger LOGGER = LogManager.getLogger(SelectCharacterPacketHandler.class);

    @Override
    public void handle(Channel channel, SelectCharacterPacket packet) {
        ConnectionState state = channel.attr(ConnectionState.KEY).get();

        if (state != ConnectionState.SELECTING_CHARACTER) {
            LOGGER.warn("Unexpected login session state, got={}, expected=SELECTING_CHARACTER", state);
            channel.disconnect();
        }
    }
}
