package work.fking.pangya.login.packet.handler;

import io.netty.channel.Channel;
import lombok.extern.log4j.Log4j2;
import work.fking.pangya.login.model.LoginSession;
import work.fking.pangya.login.model.LoginState;
import work.fking.pangya.login.packet.inbound.SetNicknamePacket;
import work.fking.pangya.networking.protocol.InboundPacketHandler;

import javax.inject.Singleton;

@Log4j2
@Singleton
public class SetNicknamePacketHandler implements InboundPacketHandler<SetNicknamePacket> {

    @Override
    public void handle(Channel channel, SetNicknamePacket packet) {
        LoginSession session = channel.attr(LoginSession.KEY).get();

        if (session.getState() != LoginState.SELECTED_NICKNAME) {
            LOGGER.warn("Unexpected login session state, got={}, expected=SELECTED_NICKNAME", session.getState());
            channel.disconnect();
            return;
        }
        channel.writeAndFlush(null);
    }
}
