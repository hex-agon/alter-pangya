package work.fking.pangya.game.packet.handler.room;

import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.fking.pangya.game.packet.inbound.room.LeaveRoomPacket;
import work.fking.pangya.game.packet.outbound.RoomResponses;
import work.fking.pangya.networking.protocol.InboundPacketHandler;

public class LeaveRoomPacketHandler implements InboundPacketHandler<LeaveRoomPacket> {

    private static final Logger LOGGER = LogManager.getLogger(LeaveRoomPacketHandler.class);

    @Override
    public void handle(Channel channel, LeaveRoomPacket packet) {
        LOGGER.debug(packet);
        channel.writeAndFlush(RoomResponses.leaveSuccess());
    }
}
