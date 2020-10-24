package work.fking.pangya.login.packet.handler;

import io.netty.channel.Channel;
import lombok.extern.log4j.Log4j2;
import work.fking.pangya.login.model.LoginSession;
import work.fking.pangya.login.model.LoginState;
import work.fking.pangya.login.packet.inbound.CheckNicknamePacket;
import work.fking.pangya.login.packet.outbound.CheckNicknameResultPacket;
import work.fking.pangya.networking.protocol.InboundPacketHandler;

import javax.inject.Singleton;

@Log4j2
@Singleton
public class CheckNicknamePacketHandler implements InboundPacketHandler<CheckNicknamePacket> {

    @Override
    public void handle(Channel channel, CheckNicknamePacket packet) {
        LoginSession session = channel.attr(LoginSession.KEY).get();

        if (session.getState() != LoginState.SELECTING_NICKNAME) {
            LOGGER.warn("Unexpected login session state, got={}, expected=SELECTING_NICKNAME", session.getState());
            channel.disconnect();
            return;
        }
        session.updateState(LoginState.SELECTED_NICKNAME); // only if the nickname is available
        channel.writeAndFlush(CheckNicknameResultPacket.available(packet.getNickname()));
    }
}
